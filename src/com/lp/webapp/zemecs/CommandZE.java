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
package com.lp.webapp.zemecs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.SperrenDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosReport;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.fastlanereader.generated.FLRReise;
import com.lp.server.personal.fastlanereader.generated.FLRTaetigkeit;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.GleitzeitsaldoDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalfingerDto;
import com.lp.server.personal.service.PersonalverfuegbarkeitDto;
import com.lp.server.personal.service.PersonalzutrittsklasseDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitverteilungDto;
import com.lp.server.personal.service.ZutrittscontrollerDto;
import com.lp.server.personal.service.ZutrittscontrollerFac;
import com.lp.server.personal.service.ZutrittsklasseDto;
import com.lp.server.personal.service.ZutrittslogDto;
import com.lp.server.personal.service.ZutrittsobjektDto;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TextDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.webapp.frame.Command;
import com.lp.webapp.frame.TheApp;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/02/06 10:14:29 $
 */
public class CommandZE extends Command {

	private static final String sUser = "lpwebappzemecs";

	private String mutex = "";

	public CommandZE(String sJSPI) {
		super(sJSPI);
	}

	/**
	 * getCommand
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	protected String getCommand(HttpServletRequest request) {
		String command = super.getCommand(request);
		String record = request.getParameter("record");
		String recordfingerprint = request.getParameter("recordfingerprint");

		if (command == null && record != null) {

			command = TheApp.CMD_ZE_MECS_ZEITBUCHEN;
		} else if (command == null && recordfingerprint != null) {

			command = TheApp.CMD_ZE_MECS_ZEITBUCHENFINGERPRINT;
		}
		return command;

	}

	private String getCookieValue(String key, HttpServletRequest request) {
		if (request != null && request.getCookies() != null) {

			for (int i = 0; i < request.getCookies().length; i++) {
				Cookie cookie = request.getCookies()[i];
				if (cookie.getName().equals(key)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public synchronized String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		super.execute(request, response);

		if (request.getCookies() != null) {
			for (int i = 0; i < request.getCookies().length; i++) {
				Cookie cookie = request.getCookies()[i];
				cookie.setMaxAge(10000000);
				response.addCookie(cookie);
			}
		}

		String mandant = request.getParameter("mandant");

		Locale localeLogon = getMandantFac().getLocaleDesHauptmandanten();

		String locale = request.getParameter("locale");

		String localeCookie = getCookieValue("locale", request);

		if (localeCookie != null && localeCookie.length() > 3) {
			locale = localeCookie;
		}

		if (locale != null && locale.length() > 3) {
			localeLogon = new Locale(locale.substring(0, 2), locale.substring(
					2, 4));
		}

		TheClientDto theclientDto = null;
		synchronized (mutex) {
			theclientDto = getLogonFac().logon(
					Helper.getFullUsername(sUser),
					Helper.getMD5Hash((sUser + new String("lpwebappzemecs"))
							.toCharArray()), localeLogon, null, null,
					new Timestamp(System.currentTimeMillis()));

			if (mandant != null && mandant.length() > 0) {

				theclientDto = getLogonFac()
						.logon(Helper.getFullUsername(sUser),
								Helper.getMD5Hash((sUser + "lpwebappzemecs")
										.toCharArray()), localeLogon, mandant,
								theclientDto,
								new Timestamp(System.currentTimeMillis()));
			} else {
				BenutzerDto benutzerDto = getBenutzerFac()
						.benutzerFindByCBenutzerkennung(
								"lpwebappzemecs",
								new String(Helper.getMD5Hash("lpwebappzemecs"
										+ "lpwebappzemecs")));
				mandant = benutzerDto.getMandantCNrDefault();
			}
		}

		getTheClient(request, response).setTheClientDto(theclientDto);

		if (command.equals(TheApp.CMD_ZE_BDESTATION)) {
			String ausweis = request.getParameter("ausweis");
			getTheClient(request, response).setSMsg("");

			if (ausweis != null && ausweis.length() > 1) {
				// Personal suchen
				PersonalDto personalDto = getPersonalFac()
						.personalFindByCAusweis(ausweis.substring(2));
				if (personalDto != null) {
					personalDto.setPartnerDto(getPartnerFac()
							.partnerFindByPrimaryKey(
									personalDto.getPartnerIId(), theclientDto));

					HashMap<String, Serializable> hmParameter = new HashMap<String, Serializable>();
					ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
					zeitdatenDto.setCWowurdegebucht("BDE-Station "
							+ request.getRemoteHost());
					zeitdatenDto.setPersonalIId(personalDto.getIId());
					hmParameter.put("zeitdaten", zeitdatenDto);
					hmParameter.put("person", personalDto.getPartnerDto()
							.formatFixTitelName1Name2());
					getTheClient(request, response).setData(hmParameter);
					setSJSPNext("bdestation2.jsp");
					return getSJSPNext();
				} else {
					getTheClient(request, response).setSMsg(
							"Ausweis " + ausweis
									+ " bei diesem Mandanten nicht gefunden! ");
				}
			} else {
				getTheClient(request, response).setSMsg("");
			}
		}
		if (command.equals(TheApp.CMD_ZE_BDESTATION2)) {

			HashMap<String, Serializable> hmParameter = (HashMap<String, Serializable>) getTheClient(
					request, response).getData();
			ZeitdatenDto zeitdatenDto = (ZeitdatenDto) hmParameter
					.get("zeitdaten");
			zeitdatenDto.setTZeit(new Timestamp(System.currentTimeMillis()));

			String option = request.getParameter("option");
			getTheClient(request, response).setSMsg("");

			ParametermandantDto parameterBdeMitTaetigkeitDto = getParameterFac()
					.getMandantparameter(mandant,
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_BDE_MIT_TAETIGKEIT);

			Boolean bBdeMitTaetigkeit = (Boolean) parameterBdeMitTaetigkeitDto
					.getCWertAsObject();
			com.lp.server.artikel.service.ArtikelDto artikelDtoDefaultArbeiztszeit = null;

			if (bBdeMitTaetigkeit == false) {
				ParametermandantDto parameterDtoDefaultarbeitszeit = getParameterFac()
						.getMandantparameter(
								mandant,
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL);

				if (parameterDtoDefaultarbeitszeit != null
						&& parameterDtoDefaultarbeitszeit.getCWert() != null
						&& !parameterDtoDefaultarbeitszeit.getCWert().trim()
								.equals("")) {
					try {
						artikelDtoDefaultArbeiztszeit = getArtikelFac()
								.artikelFindByCNr(
										parameterDtoDefaultarbeitszeit
												.getCWert(),
										theclientDto);
						zeitdatenDto
								.setArtikelIId(artikelDtoDefaultArbeiztszeit
										.getIId());

					} catch (RemoteException ex2) {
						myLogger.error("Default-Arbeitszeitartikel "
								+ parameterDtoDefaultarbeitszeit.getCWert()
								+ " nicht vorhanden.", ex2);
						setSJSPNext("bdestation.jsp");
						return getSJSPNext();
					}

				} else {
					myLogger.error("Default-Arbeitszeitartikel "
							+ parameterDtoDefaultarbeitszeit.getCWert()
							+ " nicht definiert.");
					setSJSPNext("bdestation.jsp");
					return getSJSPNext();
				}
			}

			if (option != null && option.length() > 2) {

				// Auftrag
				if (option.substring(0, 2).equals("$A")
						|| option.substring(0, 3).equals("$EA")) {
					try {

						ParametermandantDto parameterDto = getParameterFac()
								.getMandantparameter(
										mandant,
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR);

						if (parameterDto != null) {
							if (parameterDto.getCWert() != null
									&& parameterDto.getCWert().equals("4")) {
								if (option.charAt(4) == 47) {
									option = "$A"
											+ Helper.konvertiereDatum2StelligAuf4Stellig(option
													.substring(2, 4))
											+ option.substring(4);
								}
							}
						}
						AuftragDto auftragDto = null;
						if (option.substring(0, 2).equals("$A")) {

							auftragDto = getAuftragFac()
									.auftragFindByMandantCNrCNr(mandant,
											option.substring(2), theclientDto);
						} else {
							auftragDto = getAuftragFac()
									.auftragFindByMandantCNrCNr(mandant,
											option.substring(3), theclientDto);

						}

						AuftragpositionDto[] auftragpositionDtos = getAuftragpositionFac()
								.auftragpositionFindByAuftrag(
										auftragDto.getIId());

						if (auftragDto
								.getAuftragstatusCNr()
								.equals(com.lp.server.auftrag.service.AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
							setSJSPNext("bdestation.jsp");
							getTheClient(request, response).setSMsg(
									"Auf Auftrag "
											+ option.substring(2)
											+ " mit Status "
											+ auftragDto.getAuftragstatusCNr()
													.trim()
											+ " darf nicht gebucht werden! ");
							return getSJSPNext();
						} else {
							if (auftragpositionDtos != null
									&& auftragpositionDtos.length > 0) {
								zeitdatenDto
										.setIBelegartpositionid(auftragpositionDtos[0]
												.getIId());
								zeitdatenDto
										.setIBelegartid(auftragpositionDtos[0]
												.getBelegIId());
								zeitdatenDto
										.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
								hmParameter.put("beleg",
										"A" + option.substring(2));

								MaschineDto maschineDto = new MaschineDto();
								maschineDto.setCBez("");
								hmParameter.put("maschine", maschineDto);

								if (option.substring(0, 2).equals("$A")) {

									if (bBdeMitTaetigkeit == false) {
										setSJSPNext("bdestation.jsp");
										getTheClient(request, response)
												.setSMsg(
														getMeldungGebuchtFuerBDE(
																getTheClient(
																		request,
																		response)
																		.getData(),
																artikelDtoDefaultArbeiztszeit
																		.getCNr(),
																theclientDto));

										getZeiterfassungsFac().createZeitdaten(
												zeitdatenDto, true, true,
												false, theclientDto);
										return getSJSPNext();

									} else {
										setSJSPNext("bdestation4.jsp");
									}
								} else {
									hmParameter.put("beleg",
											"A" + option.substring(3));
									setSJSPNext("bdestation3gutschlecht.jsp");

								}

								return getSJSPNext();
							} else {
								getTheClient(request, response).setSMsg(
										"Auftrag " + option.substring(2)
												+ " hat keine Positionen! ");
								setSJSPNext("bdestation.jsp");
								return getSJSPNext();
							}
						}

					} catch (EJBExceptionLP ex) {
						getTheClient(request, response)
								.setSMsg(
										"Auftrag '"
												+ option.substring(2)
												+ "' bei diesem Mandanten nicht gefunden! ");
						setSJSPNext("bdestation.jsp");
						return getSJSPNext();
					}
				} // Los
				else if (option.substring(0, 2).equals("$L")
						|| option.substring(0, 3).equals("$EL")) {
					try {

						ParametermandantDto parameterDto = getParameterFac()
								.getMandantparameter(
										mandant,
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR);

						if (parameterDto != null) {
							if (parameterDto.getCWert() != null
									&& parameterDto.getCWert().equals("4")) {
								if (option.charAt(4) == 47) {
									option = "$L"
											+ Helper.konvertiereDatum2StelligAuf4Stellig(option
													.substring(2, 4))
											+ option.substring(4);
								}
							}
						}

						com.lp.server.fertigung.service.LosDto losDto = null;

						if (option.substring(0, 2).equals("$L")) {
							losDto = getFertigungFac().losFindByCNrMandantCNr(
									option.substring(2), mandant);

						} else {
							losDto = getFertigungFac().losFindByCNrMandantCNr(
									option.substring(3), mandant);

						}

						// WH 18-01-2006: Los benoetigt keine Positionen

						if (losDto
								.getStatusCNr()
								.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_ANGELEGT)
								|| losDto
										.getStatusCNr()
										.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_AUSGEGEBEN)
								|| losDto
										.getStatusCNr()
										.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_GESTOPPT)
								|| losDto
										.getStatusCNr()
										.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_ERLEDIGT)
								|| losDto
										.getStatusCNr()
										.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_STORNIERT)) {
							getTheClient(request, response).setSMsg(
									"Auf Los " + option.substring(2)
											+ " mit Status "
											+ losDto.getStatusCNr().trim()
											+ " darf nicht gebucht werden! ");
							setSJSPNext("bdestation.jsp");
							return getSJSPNext();
						} else {
							zeitdatenDto.setIBelegartid(losDto.getIId());
							zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_LOS);

							if (option.substring(0, 2).equals("$L")) {

								hmParameter.put("beleg",
										"L" + option.substring(2));

								if (bBdeMitTaetigkeit == false) {
									setSJSPNext("bdestation.jsp");
									getTheClient(request, response)
											.setSMsg(
													getMeldungGebuchtFuerBDE(
															getTheClient(
																	request,
																	response)
																	.getData(),
															artikelDtoDefaultArbeiztszeit
																	.getCNr(),
															theclientDto));

									getZeiterfassungsFac().createZeitdaten(
											zeitdatenDto, true, true, true,
											theclientDto);
									return getSJSPNext();
								} else {
									setSJSPNext("bdestation3.jsp");
								}
							} else {
								hmParameter.put("beleg",
										"L" + option.substring(3));
								setSJSPNext("bdestation3gutschlecht.jsp");
							}

							return getSJSPNext();

						}
					} catch (EJBExceptionLP ex) {
						getTheClient(request, response)
								.setSMsg(
										"Los '"
												+ option.substring(2)
												+ "' bei diesem Mandanten nicht gefunden! ");
						setSJSPNext("bdestation.jsp");
						return getSJSPNext();
					}

				} // Kombi-Code
				else if (option.length() > 1
						&& option.substring(0, 2).equals("$V")) {
					setSJSPNext("bdestation.jsp");

					if (option.length() < 12) {

						getTheClient(request, response).setSMsg(
								"Kombicode muss 10-Stellig sein ");
						return getSJSPNext();
					}

					try {

						ParametermandantDto parameter = getParameterFac()
								.getMandantparameter(
										theclientDto.getMandant(),
										ParameterFac.KATEGORIE_FERTIGUNG,
										ParameterFac.PARAMETER_LOSNUMMER_AUFTRAGSBEZOGEN);
						int iVerlaengerungLosnummer = 0;
						if ((Integer) parameter.getCWertAsObject() >= 1) {
							iVerlaengerungLosnummer = 2;
						}

						com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
								.losFindByCNrMandantCNr(
										option.substring(2,
												12 + iVerlaengerungLosnummer),
										mandant);

						// WH 18-01-2006: Los benoetigt keine Positionen

						if (losDto
								.getStatusCNr()
								.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_ANGELEGT)
								|| losDto
										.getStatusCNr()
										.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_AUSGEGEBEN)
								|| losDto
										.getStatusCNr()
										.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_GESTOPPT)
								|| losDto
										.getStatusCNr()
										.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_ERLEDIGT)
								|| losDto
										.getStatusCNr()
										.equals(com.lp.server.fertigung.service.FertigungFac.STATUS_STORNIERT)) {
							getTheClient(request, response).setSMsg(
									"Auf Los " + option.substring(2)
											+ " mit Status "
											+ losDto.getStatusCNr().trim()
											+ " darf nicht gebucht werden! ");
						} else {
							zeitdatenDto.setIBelegartid(losDto.getIId());
							zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_LOS);

							String maschine = option.substring(
									12 + iVerlaengerungLosnummer,
									14 + iVerlaengerungLosnummer);

							String taetigkeit = option
									.substring(14 + iVerlaengerungLosnummer);

							MaschineDto maschineDto = new MaschineDto();
							maschineDto.setCBez(maschine);
							hmParameter.put("maschine", maschineDto);

							hmParameter
									.put("beleg",
											"L"
													+ option.substring(
															2,
															12 + iVerlaengerungLosnummer));

							com.lp.server.artikel.service.ArtikelDto artikelDto = null;
							try {
								artikelDto = getArtikelFac().artikelFindByCNr(
										taetigkeit, theclientDto);

								zeitdatenDto.setArtikelIId(getArtikelFac()
										.artikelFindByCNr(taetigkeit,
												theclientDto).getIId());

							} catch (RemoteException ex2) {
								getTheClient(request, response).setSMsg(
										"T\u00E4tigkeit '" + taetigkeit
												+ "' nicht gefunden! ");
								return getSJSPNext();
							}

							com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtos = getFertigungFac()
									.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
											losDto.getIId(),
											artikelDto.getIId());

							if (dtos != null && dtos.length > 0) {

								if (!maschine.trim().equals("")
										&& !maschine.equals("--")) {

									try {
										Integer maschineIId = getZeiterfassungsFac()
												.maschineFindByCIdentifikationsnr(
														maschine).getIId();

										com.lp.server.fertigung.service.LossollarbeitsplanDto[] sollaDtos = getFertigungFac()
												.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
														zeitdatenDto
																.getIBelegartid(),
														zeitdatenDto
																.getArtikelIId());

										if (sollaDtos != null
												&& sollaDtos.length > 0) {
											MaschinenzeitdatenDto maschinenzeitdatenDto = new MaschinenzeitdatenDto();
											maschinenzeitdatenDto
													.setLossollarbeitsplanIId(sollaDtos[0]
															.getIId());
											maschinenzeitdatenDto
													.setMaschineIId(maschineIId);
											maschinenzeitdatenDto
													.setPersonalIIdGestartet(zeitdatenDto
															.getPersonalIId());
											maschinenzeitdatenDto
													.setTVon(zeitdatenDto
															.getTZeit());
											getZeiterfassungsFac()
													.createMaschinenzeitdaten(
															maschinenzeitdatenDto,
															theclientDto);
										}

									} catch (RemoteException ex2) {
										getTheClient(request, response)
												.setSMsg(
														"Maschine '"
																+ maschine
																+ "' nicht gefunden! ");
										return getSJSPNext();
									}
								}

								// PJ 15388
								if (maschine.equals("--")) {
									hmParameter.put("fertig", "");
									if (dtos != null && dtos.length > 0) {

										LossollarbeitsplanDto dto = dtos[0];
										dto.setBFertig(Helper
												.boolean2Short(true));

										try {
											getFertigungFac()
													.updateLossollarbeitsplan(
															dto, theclientDto);
											getTheClient(request, response)
													.setSMsg(
															getMeldungGebuchtFuerBDE(
																	getTheClient(
																			request,
																			response)
																			.getData(),
																	taetigkeit
																			.substring(2),
																	theclientDto));
											return getSJSPNext();
										} catch (EJBExceptionLP ex2) {
											getTheClient(request, response)
													.setSMsg(
															"Fehler beim Buchen!");
											return getSJSPNext();
										}
									} else {
										getTheClient(request, response)
												.setSMsg(
														"Das Los "
																+ option.substring(
																		2,
																		12 + iVerlaengerungLosnummer)
																+ " hat keinen entsprechen Arbeitsgang mit der Artikelnummer "
																+ taetigkeit
																		.substring(2));
										return getSJSPNext();
									}
								}

								zeitdatenDto.setIBelegartpositionid(dtos[0]
										.getIId());
							} else {
								com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtosErstePosition = getFertigungFac()
										.lossollarbeitsplanFindByLosIId(
												losDto.getIId());
								if (dtosErstePosition != null
										&& dtosErstePosition.length > 0) {
									zeitdatenDto
											.setIBelegartpositionid(dtosErstePosition[0]
													.getIId());
								} else {
									// Bemerkung
									getTheClient(request, response).setSMsg(
											"Los " + option.substring(2)
													+ " hat keine Positionen");
									return getSJSPNext();

								}
							}

							try {
								getZeiterfassungsFac().createZeitdaten(
										zeitdatenDto, true, true, true,
										theclientDto);
								getTheClient(request, response).setSMsg(
										getMeldungGebuchtFuerBDE(
												getTheClient(request, response)
														.getData(), taetigkeit,
												theclientDto));

							} catch (EJBExceptionLP ex2) {
								getTheClient(request, response).setSMsg(
										"Fehler beim Buchen!");
								return getSJSPNext();
							}
						}
					} catch (EJBExceptionLP ex) {
						getTheClient(request, response)
								.setSMsg(
										"Los '"
												+ option.substring(2)
												+ "' bei diesem Mandanten nicht gefunden! ");
						return getSJSPNext();

					}
					return getSJSPNext();
				} // Sondertaetigkeit
				else {

					if (option.substring(1).equals("SALDO")) {

						java.sql.Timestamp ts = new java.sql.Timestamp(
								System.currentTimeMillis() - 3600000 * 24);
						ts = com.lp.util.Helper.cutTimestamp(ts);

						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(ts.getTime());

						String saldoMitUrlaub = "";
						try {
							saldoMitUrlaub = getZeiterfassungsFac()
									.erstelleMonatsAbrechnungFuerBDE(
											zeitdatenDto.getPersonalIId(),
											new Integer(c.get(Calendar.YEAR)),
											new Integer(c.get(Calendar.MONTH)),
											false,
											new java.sql.Date(ts.getTime()),
											theclientDto, true, false);

						} catch (EJBExceptionLP ex7) {

							if (ex7.getCause() instanceof EJBExceptionLP) {
								EJBExceptionLP e = (EJBExceptionLP) ex7
										.getCause();
								if (e != null
										&& e.getCode() == EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM) {
									getTheClient(request, response)
											.setSMsg(
													new String(
															"FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM"));
									return getSJSPNext();

								}
							}
							getTheClient(request, response).setSMsg(
									new String(ex7.getMessage()));
							setSJSPNext("bdestation.jsp");

						}

						getTheClient(request, response).setSMsg(saldoMitUrlaub);

						setSJSPNext("bdestation.jsp");
						return getSJSPNext();

					} else if (option.substring(1).equals("TAGESSALDO")) {

						java.sql.Timestamp ts = new java.sql.Timestamp(
								System.currentTimeMillis() - 3600000 * 24);

						Double d = getZeiterfassungsFac()
								.berechneTagesArbeitszeit(
										zeitdatenDto.getPersonalIId(),
										new java.sql.Date(System
												.currentTimeMillis()),
										theclientDto);

						StringBuffer sb = new StringBuffer();
						sb.append("Tagesarbeitszeit bis jetzt: "
								+ Helper.rundeKaufmaennisch(
										new BigDecimal(d.doubleValue()), 2)
										.doubleValue() + "h");
						sb.append("\r\n");

						getTheClient(request, response).setSMsg(new String(sb));
						setSJSPNext("bdestation.jsp");
						return getSJSPNext();

					} else {

						try {
							TaetigkeitDto taetigkeitDto = getZeiterfassungsFac()
									.taetigkeitFindByCNr(option.substring(1),
											theclientDto);

							zeitdatenDto.setTaetigkeitIId(taetigkeitDto
									.getIId());
							getZeiterfassungsFac().createZeitdaten(
									zeitdatenDto, true, true, true,
									theclientDto);
							getTheClient(request, response).setSMsg(
									getMeldungGebuchtFuerBDE(hmParameter,
											option.substring(1), theclientDto));
						} catch (EJBExceptionLP ex1) {
							getTheClient(request, response).setSMsg(
									"Sondert\u00E4tigkeit '"
											+ option.substring(1)
											+ "' nicht gefunden! ");
						}
						hmParameter.put("zeitdaten", zeitdatenDto);
						setSJSPNext("bdestation.jsp");

					}
					getTheClient(request, response).setData(hmParameter);
					return getSJSPNext();
				}
			}

		} else if (command.equals(TheApp.CMD_ZE_BDESTATION3)) {

			HashMap<String, Serializable> hmParameter = (HashMap<String, Serializable>) getTheClient(
					request, response).getData();
			ZeitdatenDto zeitdatenDto = (ZeitdatenDto) hmParameter
					.get("zeitdaten");
			zeitdatenDto.setTZeit(new Timestamp(System.currentTimeMillis()));

			String option = request.getParameter("option");
			getTheClient(request, response).setSMsg("");

			if (option != null && option.length() > 0) {

				if (option.equals("$PLUS")) {
					// CK: PJ5589
					String beleg = (String) hmParameter.get("beleg");

					if (beleg.substring(0, 1).equals("L")) {
						com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
								.losFindByCNrMandantCNr(beleg.substring(1),
										mandant);

						ZeitverteilungDto zeitverteilungDto = new ZeitverteilungDto();
						zeitverteilungDto.setLosIId(losDto.getIId());
						zeitverteilungDto.setTZeit(new Timestamp(System
								.currentTimeMillis()));
						zeitverteilungDto.setPersonalIId(zeitdatenDto
								.getPersonalIId());
						try {
							getZeiterfassungsFac().createZeitverteilung(
									zeitverteilungDto, theclientDto);
						} catch (EJBExceptionLP e) {
							hmParameter.remove("beleg");
							getTheClient(request, response)
									.setData(hmParameter);
							getTheClient(request, response)
									.setSMsg(
											"Los '"
													+ losDto.getCNr()
													+ "' wurde bereits mit $PLUS gebucht");
							setSJSPNext("bdestation2.jsp");
							return getSJSPNext();
						}

					} else {
						getTheClient(request, response).setSMsg(
								"$PLUS ist nur f\u00FCr Lose m\u00F6glich.");
						setSJSPNext("bdestation.jsp");
						return getSJSPNext();
					}

					hmParameter.remove("beleg");
					getTheClient(request, response).setData(hmParameter);
					setSJSPNext("bdestation2.jsp");
					return getSJSPNext();

				} else if (option.equals("$STORNO")) {
					getZeiterfassungsFac()
							.removeZeitverteilungByPersonalIIdUndTag(
									zeitdatenDto.getPersonalIId(),
									new Timestamp(System.currentTimeMillis()));
					hmParameter.remove("beleg");
					getTheClient(request, response).setData(hmParameter);
					setSJSPNext("bdestation2.jsp");
				} else if (option.equals("$SPERREN")) {

					String beleg = (String) hmParameter.get("beleg");
					if (beleg.substring(0, 1).equals("L")) {
						com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
								.losFindByCNrMandantCNr(beleg.substring(1),
										mandant);

						if (losDto.getStuecklisteIId() != null) {
							Integer artikelIId = getStuecklisteFac()
									.stuecklisteFindByPrimaryKey(
											losDto.getStuecklisteIId(),
											theclientDto).getArtikelIId();

							SperrenDto sDto = getArtikelFac()
									.sperrenFindBDurchfertigung(theclientDto);

							if (sDto != null) {

								ArtikelsperrenDto aspDtoVorhanden = getArtikelFac()
										.artikelsperrenFindByArtikelIIdSperrenIIdOhneExc(
												artikelIId, sDto.getIId());
								if (aspDtoVorhanden == null) {
									ArtikelsperrenDto spDto = new ArtikelsperrenDto();
									spDto.setArtikelIId(artikelIId);
									spDto.setSperrenIId(sDto.getIId());

									PersonalDto pDto = getPersonalFac()
											.personalFindByPrimaryKey(
													zeitdatenDto
															.getPersonalIId(),
													theclientDto);

									String grund = beleg
											+ " "
											+ pDto.getPartnerDto()
													.getCName1nachnamefirmazeile1()
											+ " "
											+ pDto.getPartnerDto()
													.getCName2vornamefirmazeile2();

									if (grund.length() > 80) {
										grund = grund.substring(0, 79);
									}

									spDto.setCGrund(grund);
									getArtikelFac().createArtikelsperren(spDto,
											theclientDto);
									getTheClient(request, response)
											.setSMsg(
													"Los "
															+ losDto.getCNr()
															+ " durch Fertigung gesperrt.");
									setSJSPNext("bdestation.jsp");
									return getSJSPNext();
								} else {
									getTheClient(request, response)
											.setSMsg(
													"St\u00FCckliste bereits durch Fertigung gesperrt.");
									setSJSPNext("bdestation.jsp");
									return getSJSPNext();
								}
							} else {
								getTheClient(request, response)
										.setSMsg(
												"Fertigungssperre in Grunddaten nicht definiert.");
								setSJSPNext("bdestation.jsp");
								return getSJSPNext();
							}

						}

						setSJSPNext("bdestation.jsp");
						return getSJSPNext();
					}

				} else if (option.equals("$FERTIG")) {
					hmParameter.put("fertig", "");
					MaschineDto maschineDto = new MaschineDto();
					maschineDto.setCBez("Fertig");
					hmParameter.put("maschine", maschineDto);
					getTheClient(request, response).setData(hmParameter);
					setSJSPNext("bdestation4.jsp");
					return getSJSPNext();
				} else {

					setSJSPNext("bdestation.jsp");
					// MASCHINE
					if (option.substring(0, 2).equals("$M")) {

						String maschine = option.substring(2);

						try {
							MaschineDto maschineDto = getZeiterfassungsFac()
									.maschineFindByCIdentifikationsnr(maschine);
							hmParameter.put("zeitdaten", zeitdatenDto);
							hmParameter.put("maschine", maschineDto);
							setSJSPNext("bdestation4.jsp");
							return getSJSPNext();

						} catch (EJBExceptionLP ex2) {
							getTheClient(request, response).setSMsg(
									"Maschine '" + maschine
											+ "' nicht gefunden! ");
							return getSJSPNext();
						}

					} // TAETIGKEIT
					else {

						String taetigkeit = option.substring(2);

						com.lp.server.artikel.service.ArtikelDto artikelDto = null;
						try {
							artikelDto = getArtikelFac().artikelFindByCNr(
									taetigkeit, theclientDto);

							zeitdatenDto.setArtikelIId(getArtikelFac()
									.artikelFindByCNr(taetigkeit, theclientDto)
									.getIId());

						} catch (EJBExceptionLP ex2) {
							getTheClient(request, response).setSMsg(
									"T\u00E4tigkeit '" + taetigkeit
											+ "' nicht gefunden! ");
							return getSJSPNext();
						}

						String beleg = (String) hmParameter.get("beleg");

						if (beleg.substring(0, 1).equals("L")) {
							com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
									.losFindByCNrMandantCNr(beleg.substring(1),
											mandant);

							ZeitverteilungDto[] zvDtos = getZeiterfassungsFac()
									.zeitverteilungFindByPersonalIIdUndTag(
											zeitdatenDto.getPersonalIId(),
											new Timestamp(System
													.currentTimeMillis()));
							if (zvDtos != null & zvDtos.length > 0) {

								if (zvDtos[0].getArtikelIId() == null) {
									// Abschlussbuchung eintragen
									ZeitverteilungDto zv = zvDtos[0];
									zv.setIId(null);
									zv.setLosIId(losDto.getIId());
									zv.setArtikelIId(artikelDto.getIId());
									try {
										getZeiterfassungsFac()
												.createZeitverteilung(zv,
														theclientDto);
									} catch (EJBExceptionLP e) {
										hmParameter.remove("beleg");
										getTheClient(request, response)
												.setData(hmParameter);
										getTheClient(request, response)
												.setSMsg(
														"Los '"
																+ losDto.getCNr()
																+ "' wurde bereits mit $PLUS gebucht");
										setSJSPNext("bdestation2.jsp");
										return getSJSPNext();
									}

									hmParameter.remove("beleg");
									getTheClient(request, response).setData(
											hmParameter);
									getTheClient(request, response)
											.setSMsg(
													"Beginnbuchungen f\u00FCr 'Zeitverteilung' abgeschlossen");
									setSJSPNext("bdestation.jsp");
									return getSJSPNext();
								}
							}

							com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtos = getFertigungFac()
									.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
											losDto.getIId(),
											artikelDto.getIId());

							if (dtos != null && dtos.length > 0) {
								zeitdatenDto.setIBelegartpositionid(dtos[0]
										.getIId());
							} else {
								com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtosErstePosition = getFertigungFac()
										.lossollarbeitsplanFindByLosIId(
												losDto.getIId());
								if (dtosErstePosition != null
										&& dtosErstePosition.length > 0) {
									zeitdatenDto
											.setIBelegartpositionid(dtosErstePosition[0]
													.getIId());
								} else {
									// Bemerkung
									getTheClient(request, response).setSMsg(
											"Los " + beleg.substring(1)
													+ " hat keine Positionen");
									return getSJSPNext();

								}
							}
						}

						try {
							getZeiterfassungsFac().createZeitdaten(
									zeitdatenDto, true, true, true,
									theclientDto);
							getTheClient(request, response).setSMsg(
									getMeldungGebuchtFuerBDE(
											getTheClient(request, response)
													.getData(), taetigkeit,
											theclientDto));

							return getSJSPNext();
						} catch (EJBExceptionLP ex2) {
							getTheClient(request, response).setSMsg(
									"Fehler beim Buchen!");
							return getSJSPNext();
						}
					}
				}
			}
		} else if (command.equals(TheApp.CMD_ZE_BDESTATION3GUTSCHLECHT)) {
			HashMap<Object, Object> hmParameter = (HashMap<Object, Object>) getTheClient(
					request, response).getData();
			ZeitdatenDto zeitdatenDto = (ZeitdatenDto) hmParameter
					.get("zeitdaten");

			String gutstueck = request.getParameter("gutstueck");
			if (gutstueck.equals("")) {
				gutstueck = "0";
			}
			String schlechtstueck = request.getParameter("schlechtstueck");
			if (schlechtstueck.equals("")) {
				schlechtstueck = "0";
			}

			BigDecimal bdGutstueck = null;
			BigDecimal bdSchlechtstueck = null;
			try {
				bdGutstueck = new BigDecimal(gutstueck);
				bdSchlechtstueck = new BigDecimal(schlechtstueck);
			} catch (NumberFormatException ex9) {
				getTheClient(request, response)
						.setSMsg(
								"Gut/Schlechtst\u00FCck d\u00FCrfen nur aus Zahlen bestehen.");
				setSJSPNext("bdestation.jsp");
				return getSJSPNext();
			}

			if (bdGutstueck.doubleValue() < 0
					|| bdSchlechtstueck.doubleValue() < 0) {
				getTheClient(request, response).setSMsg(
						"Gut/Schlechtst\u00FCck m\u00FCssen Positiv sein.");
				setSJSPNext("bdestation.jsp");
				return getSJSPNext();
			}
			Integer taetigkeitIId_Ende = getZeiterfassungsFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE,
							theclientDto).getIId();
			ZeitdatenDto zeitdatenDtoEnde = new ZeitdatenDto();
			zeitdatenDtoEnde.setTZeit(new Timestamp(zeitdatenDto.getTZeit()
					.getTime() + 1000));
			zeitdatenDtoEnde.setTaetigkeitIId(taetigkeitIId_Ende);
			zeitdatenDtoEnde.setPersonalIId(zeitdatenDto.getPersonalIId());

			// Hole letzten begonnenen Auftrag und hinterlege gut/schlechtstueck
			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria liste = session
					.createCriteria(FLRZeitdaten.class);
			liste.add(Expression.eq(
					ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
					zeitdatenDto.getPersonalIId()));
			liste.add(Expression.gt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
					Helper.cutTimestamp(zeitdatenDto.getTZeit())));

			liste.addOrder(Order.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
			List<?> letzerAuftrag = liste.list();

			Iterator<?> it = letzerAuftrag.iterator();
			ZeitdatenDto letzterAuftrag = null;

			while (it.hasNext()) {
				FLRZeitdaten flrLetzerAuftrag = (FLRZeitdaten) it.next();

				if (flrLetzerAuftrag.getC_belegartnr() != null
						&& flrLetzerAuftrag.getI_belegartid() != null) {
					if (flrLetzerAuftrag.getC_belegartnr().equals(
							zeitdatenDto.getCBelegartnr())
							&& flrLetzerAuftrag.getI_belegartid().equals(
									zeitdatenDto.getIBelegartid())) {
						letzterAuftrag = getZeiterfassungsFac()
								.zeitdatenFindByPrimaryKey(
										flrLetzerAuftrag.getI_id(),
										theclientDto);
						break;
					}
				} else if (flrLetzerAuftrag.getTaetigkeit_i_id() != null
						&& flrLetzerAuftrag.getTaetigkeit_i_id().equals(
								taetigkeitIId_Ende)) {
					break;
				}

			}

			if (letzterAuftrag != null) {
				// Hier eintragen
				// letzterAuftrag.setNGut(bdGutstueck);
				// letzterAuftrag.setNSchlecht(bdSchlechtstueck);
				getZeiterfassungsFac().updateZeitdaten(letzterAuftrag,
						theclientDto);
				// und buche ENDE
				getZeiterfassungsFac().createZeitdaten(zeitdatenDtoEnde, false,
						false, false, theclientDto);

			} else {
				// was nun?
				// Beginn und ende Buchen
				getZeiterfassungsFac().createZeitdaten(zeitdatenDto, false,
						false, false, theclientDto);
				getZeiterfassungsFac().createZeitdaten(zeitdatenDtoEnde, false,
						false, false, theclientDto);

			}

			session.close();
			getTheClient(request, response).setSMsg(
					getMeldungGebuchtFuerBDE(getTheClient(request, response)
							.getData(), null, theclientDto));

			setSJSPNext("bdestation.jsp");
			return getSJSPNext();
		}

		else if (command.equals(TheApp.CMD_ZE_BDESTATION4)) {
			HashMap<?, ?> hmParameter = (HashMap<?, ?>) getTheClient(request,
					response).getData();
			ZeitdatenDto zeitdatenDto = (ZeitdatenDto) hmParameter
					.get("zeitdaten");
			zeitdatenDto.setTZeit(new Timestamp(System.currentTimeMillis()));

			String taetigkeit = request.getParameter("taetigkeit");
			getTheClient(request, response).setSMsg("");
			if (taetigkeit != null && taetigkeit.length() > 0) {

				setSJSPNext("bdestation.jsp");
				com.lp.server.artikel.service.ArtikelDto artikelDto = null;
				try {
					artikelDto = getArtikelFac().artikelFindByCNr(
							taetigkeit.substring(2), theclientDto);

					zeitdatenDto.setArtikelIId(artikelDto.getIId());
				} catch (EJBExceptionLP ex2) {
					getTheClient(request, response).setSMsg(
							"T\u00E4tigkeit '" + taetigkeit.substring(2)
									+ "' nicht gefunden! ");
					return getSJSPNext();
				}

				String beleg = (String) hmParameter.get("beleg");

				if (beleg.substring(0, 1).equals("L")) {
					com.lp.server.fertigung.service.LosDto losDto = getFertigungFac()
							.losFindByCNrMandantCNr(beleg.substring(1), mandant);

					com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
									losDto.getIId(), artikelDto.getIId());

					if (hmParameter.containsKey("fertig")) {

						if (dtos != null && dtos.length > 0) {

							LossollarbeitsplanDto dto = dtos[0];
							dto.setBFertig(Helper.boolean2Short(true));

							ParametermandantDto parameterDtoTriggerTops = getParameterFac()
									.getMandantparameter(
											mandant,
											ParameterFac.KATEGORIE_FERTIGUNG,
											ParameterFac.PARAMETER_TRIGGERT_TRUMPF_TOPS_ABLIEFERUNG);

							try {
								getFertigungFac().updateLossollarbeitsplan(dto,
										theclientDto);

								// PJ 17916
								if (parameterDtoTriggerTops.getCWert() != null
										&& parameterDtoTriggerTops.getCWert()
												.trim().length() > 0) {
									ArtikelDto aDto = getArtikelFac()
											.artikelFindByCNrMandantCNrOhneExc(
													parameterDtoTriggerTops
															.getCWert().trim(),
													theclientDto.getMandant());

									if (aDto == null) {
										getTheClient(request, response)
												.setSMsg(
														"Der Artikel, der im Parameter TRIGGERT_TRUMPF_TOPS_ABLIEFERUNG hinterlegt ist, exisitiert nicht! "
																+ parameterDtoTriggerTops
																		.getCWert());
										return getSJSPNext();
									}
									if (aDto != null
											&& aDto.getIId().equals(
													artikelDto.getIId())) {
										getFertigungFac()
												.bucheTOPSArtikelAufHauptLager(
														losDto.getIId(),
														theclientDto, null);
									}
								}

								getTheClient(request, response).setSMsg(
										getMeldungGebuchtFuerBDE(
												getTheClient(request, response)
														.getData(), taetigkeit
														.substring(2),
												theclientDto));
								return getSJSPNext();
							} catch (EJBExceptionLP ex2) {
								getTheClient(request, response).setSMsg(
										"Fehler beim Buchen!");
								return getSJSPNext();
							}
						} else {
							getTheClient(request, response)
									.setSMsg(
											"Das Los "
													+ beleg.substring(1)
													+ " hat keinen entsprechen Arbeitsgang mit der Artikelnummer "
													+ taetigkeit.substring(2));
							return getSJSPNext();
						}

					}

					if (dtos != null && dtos.length > 0) {
						zeitdatenDto.setIBelegartpositionid(dtos[0].getIId());
					} else {
						com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtosErstePosition = getFertigungFac()
								.lossollarbeitsplanFindByLosIId(losDto.getIId());
						if (dtosErstePosition != null
								&& dtosErstePosition.length > 0) {
							zeitdatenDto
									.setIBelegartpositionid(dtosErstePosition[0]
											.getIId());
						} else {
							// Bemerkung
							getTheClient(request, response).setSMsg(
									"Los " + beleg.substring(1)
											+ " hat keine Positionen");
							return getSJSPNext();

						}
					}
				}

				// Maschinenzeitdaten buchen (geht nur auf Los)

				if (hmParameter.containsKey("maschine")
						&& zeitdatenDto.getCBelegartnr() != null
						&& zeitdatenDto.getCBelegartnr().equals(
								LocaleFac.BELEGART_LOS)
						&& zeitdatenDto.getIBelegartid() != null) {
					MaschineDto maschineDto = (MaschineDto) hmParameter
							.get("maschine");

					com.lp.server.fertigung.service.LossollarbeitsplanDto[] dtos = getFertigungFac()
							.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
									zeitdatenDto.getIBelegartid(),
									zeitdatenDto.getArtikelIId());

					if (dtos != null && dtos.length > 0) {
						MaschinenzeitdatenDto maschinenzeitdatenDto = new MaschinenzeitdatenDto();
						maschinenzeitdatenDto
								.setPersonalIIdGestartet(zeitdatenDto
										.getPersonalIId());
						maschinenzeitdatenDto.setTVon(zeitdatenDto.getTZeit());
						maschinenzeitdatenDto.setLossollarbeitsplanIId(dtos[0]
								.getIId());
						maschinenzeitdatenDto.setMaschineIId(maschineDto
								.getIId());
						getZeiterfassungsFac().createMaschinenzeitdaten(
								maschinenzeitdatenDto, theclientDto);

					}
				}

				try {
					getZeiterfassungsFac().createZeitdaten(zeitdatenDto, true,
							true, false, theclientDto);
					getTheClient(request, response).setSMsg(
							getMeldungGebuchtFuerBDE(
									getTheClient(request, response).getData(),
									taetigkeit.substring(2), theclientDto));
					return getSJSPNext();
				} catch (EJBExceptionLP ex2) {
					getTheClient(request, response).setSMsg(
							"Fehler beim Buchen!");
					return getSJSPNext();
				}

			}
		} else if (command.equals(TheApp.CMD_ZE_MECS_ONLCHECK)) {
			String beleg = request.getParameter("beleg");

			if (beleg == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Parameter 'beleg' muss angegeben werden");
				return null;
			}

			beleg = beleg.trim();

			if (beleg.length() < 2) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Parameter 'beleg' muss mindestens 2 Zeichen lang sein");
				return null;
			}

			String status = null;
			BigDecimal offeneMenge = new BigDecimal(0);

			String ueberliefernErlaubt = "1";
			try {

				if (beleg.substring(0, 2).equals("$A")) {

					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByMandantCNrCNr(mandant,
									beleg.substring(2), theclientDto);
					status = auftragDto.getAuftragstatusCNr();

				} else if (beleg.substring(0, 2).equals("$L")) {
					LosDto losDto = getFertigungFac().losFindByCNrMandantCNr(
							beleg.substring(2), mandant);
					status = losDto.getStatusCNr();

					BigDecimal erledigteMenge = getFertigungFac()
							.getErledigteMenge(losDto.getIId(), theclientDto);
					offeneMenge = losDto.getNLosgroesse().subtract(
							erledigteMenge);

					if (losDto.getStuecklisteIId() != null) {
						StuecklisteDto stkDto = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(
										losDto.getStuecklisteIId(),
										theclientDto);

						if (Helper.short2boolean(stkDto.getBUeberlieferbar()) == false) {
							ueberliefernErlaubt = "0";
						}

					}

				}

			} catch (EJBExceptionLP ex8) {
				status = "Beleg existiert nicht";
			}

			StringBuffer sb = new StringBuffer();

			sb.append(Helper.fitString2Length(beleg, 40, ' '));
			sb.append(Helper.fitString2Length(status, 40, ' '));

			// Offene Menge 17 stellig

			DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
			dfs.setDecimalSeparator('.');
			DecimalFormat dFormat = new DecimalFormat("0.0000", dfs);
			if (offeneMenge.doubleValue() < 0) {
				sb.append("-");
			} else {
				sb.append(" ");
			}
			sb.append(Helper.fitString2LengthAlignRight(
					dFormat.format(offeneMenge.abs()), 16, ' '));
			sb.append(ueberliefernErlaubt);
			sb.append("\r\n");

			getTheClient(request, response).setSMsg(new String(sb));

		} else if (command.equals(TheApp.CMD_ZE_MECS_ONLINECHECK_ABL)) {
			String beleg = request.getParameter("beleg");
			String menge = request.getParameter("menge");

			if (beleg == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Parameter 'beleg' muss angegeben werden");
				return null;
			}
			if (menge == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Parameter 'menge' muss angegeben werden");
				return null;
			}

			BigDecimal nMenge = new BigDecimal(menge.trim());

			beleg = beleg.trim();

			if (beleg.length() < 2) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Parameter 'beleg' muss mindestens 2 Zeichen lang sein");
				return null;
			}

			String status = null;
			BigDecimal offeneMenge = new BigDecimal(0);
			boolean ueberliefernErlaubt = true;
			try {

				if (beleg.substring(0, 2).equals("$A")) {

					AuftragDto auftragDto = getAuftragFac()
							.auftragFindByMandantCNrCNr(mandant,
									beleg.substring(2), theclientDto);
					status = auftragDto.getAuftragstatusCNr();

				} else if (beleg.substring(0, 2).equals("$L")) {
					LosDto losDto = getFertigungFac().losFindByCNrMandantCNr(
							beleg.substring(2), mandant);
					status = losDto.getStatusCNr();

					BigDecimal erledigteMenge = getFertigungFac()
							.getErledigteMenge(losDto.getIId(), theclientDto);
					offeneMenge = losDto.getNLosgroesse().subtract(
							erledigteMenge);
					if (losDto.getStuecklisteIId() != null) {
						StuecklisteDto stkDto = getStuecklisteFac()
								.stuecklisteFindByPrimaryKey(
										losDto.getStuecklisteIId(),
										theclientDto);
						ueberliefernErlaubt = Helper.short2boolean(stkDto
								.getBUeberlieferbar());
					}

				}

			} catch (EJBExceptionLP ex8) {
				status = "Beleg existiert nicht";
			}

			StringBuffer sb = new StringBuffer();

			// Zeile1
			sb.append(Helper.fitString2Length(beleg, 40, ' '));
			sb.append(Helper.fitString2Length(status, 40, ' '));
			sb.append("\r\n");
			// Offene Menge 17 stellig

			// Zeile2

			// Zuerst 3 Stellen Fehlernummer: 000= Abliefern moeglich 001=
			// Status erlaubt kein Abliefern - 002=
			// Menge der Ablieferung zu gross

			String fehlercode = "";
			String text1 = "";
			String text2 = "";
			if (status.equals(LocaleFac.STATUS_ERLEDIGT)
					|| status.equals(LocaleFac.STATUS_STORNIERT)
					|| status.equals(LocaleFac.STATUS_ANGELEGT)
					|| status.equals(LocaleFac.STATUS_GESTOPPT)) {
				fehlercode = "001";
				text1 = "Nicht erlaubt!";
				text2 = "Status: " + status;
			} else {

				if (nMenge.doubleValue() <= offeneMenge.doubleValue()) {
					// Wenn Abliefermenge kleiner als Offene Menge, dann =OK
					fehlercode = "000";
					text1 = "Ablieferung";
					text2 = "erlaubt";
				} else {
					if (ueberliefernErlaubt == false) {
						fehlercode = "002";
						text1 = "Nicht erlaubt!";

						DecimalFormatSymbols dfs = DecimalFormatSymbols
								.getInstance();
						dfs.setDecimalSeparator('.');
						DecimalFormat dFormat = new DecimalFormat("#####0", dfs);
						text2 = "Nur "
								+ Helper.fitString2LengthAlignRight(
										dFormat.format(offeneMenge), 6, ' ')
								+ " Stk offen";
					} else {
						fehlercode = "000";
						text1 = "Ablieferung";
						text2 = "erlaubt";
					}
				}
			}
			sb.append(fehlercode);

			// 37 Leerstellen
			sb.append(Helper.fitString2Length("", 37, ' '));

			// Text1
			sb.append(Helper.fitString2Length(text1, 20, ' '));
			// Text2
			sb.append(Helper.fitString2Length(text2, 20, ' '));

			sb.append("\r\n");

			getTheClient(request, response).setSMsg(new String(sb));

		} else if (command.equals(TheApp.CMD_ZE_MECS_AUSWEISE)) {
			String fingerprint = request.getParameter("fingerprint");

			if (fingerprint != null) {
				StringBuffer sb = new StringBuffer();
				PersonalfingerDto[] personalfingerDtos = getZutrittscontrollerFac()
						.personalfingerFindAll();
				for (int i = 0; i < personalfingerDtos.length; i++) {
					PersonalfingerDto personalfingerDto = personalfingerDtos[i];
					String id = personalfingerDto.getIId() + "";
					id = Helper.fitString2LengthAlignRight(id, 8, '0');
					StringBuffer tmp = new StringBuffer();
					// unbedingt nach ausweis sortieren
					tmp.setLength(0);
					tmp.append(Helper.fitString2Length(id, 20, ' '));

					PersonalDto personalDto = getPersonalFac()
							.personalFindByPrimaryKey(
									personalfingerDto.getPersonalIId(),
									theclientDto);

					tmp.append(Helper.fitString2LengthAlignRight(
							personalDto.getCPersonalnr() + "", 5, '0')); // persnr
					tmp.append(Helper.fitString2Length("", 3, ' ')); // zutrkl

					String sVorname = personalDto.getPartnerDto()
							.getCName2vornamefirmazeile2();
					String sNachname = personalDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();

					if (sVorname == null) {
						sVorname = "";
					}
					tmp.append(Helper.fitString2Length(sVorname + " "
							+ sNachname, 25, ' ')); // name
					sb.append(tmp).append("\r\n");

				}
				getTheClient(request, response).setSMsg(new String(sb));
			} else {

				// Ausweisnummern holen
				StringBuffer sb = new StringBuffer();
				PersonalDto[] personalDtos = getPersonalFac()
						.personalFindByCAusweisSortiertNachCAusweis();

				ParametermandantDto parameterDto = getParameterFac()
						.getMandantparameter(
								mandant,
								ParameterFac.KATEGORIE_PERSONAL,
								ParameterFac.PARAMETER_LEAD_IN_AUSWEISNUMMER_MECS);

				String leadIn = "";
				if (parameterDto.getCWert() != null) {
					leadIn = parameterDto.getCWert().trim();
				}

				for (int i = 0; i < personalDtos.length; i++) {
					PersonalDto personalDto = personalDtos[i];
					personalDto.setPartnerDto(getPartnerFac()
							.partnerFindByPrimaryKey(
									personalDto.getPartnerIId(), theclientDto));
					StringBuffer tmp = new StringBuffer();
					// unbedingt nach ausweis sortieren
					tmp.setLength(0);
					tmp.append(Helper.fitString2Length(
							leadIn + personalDto.getCAusweis(), 20, ' ')); // ausweis
					tmp.append(Helper.fitString2LengthAlignRight(
							personalDto.getCPersonalnr() + "", 5, '0')); // persnr
					tmp.append(Helper.fitString2Length("", 3, ' ')); // zutrkl

					String sVorname = personalDto.getPartnerDto()
							.getCName2vornamefirmazeile2();
					String sNachname = personalDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();

					if (sVorname == null) {
						sVorname = "";
					}

					tmp.append(Helper.fitString2Length(sVorname + " "
							+ sNachname, 25, ' ')); // name
					sb.append(tmp).append("\r\n");

				}

				getTheClient(request, response).setSMsg(new String(sb));
			}
		} else if (command.equals(TheApp.CMD_ZE_MECS_ERLAUBTETAETIGKEITEN)) {

			Session session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria liste = session
					.createCriteria(FLRTaetigkeit.class);
			liste.add(Expression.eq(
					ZeiterfassungFac.FLR_TAETIGKEIT_B_BDEBUCHBAR,
					Helper.boolean2Short(true)));
			liste.addOrder(Order.asc("c_nr"));
			List<?> lReisezeiten = liste.list();
			Iterator<?> it = lReisezeiten.iterator();

			StringBuffer sb = new StringBuffer();

			while (it.hasNext()) {
				FLRTaetigkeit flrTaetigkeit = (FLRTaetigkeit) it.next();
				StringBuffer tmp = new StringBuffer();

				tmp.setLength(0);
				tmp.append('$');
				tmp.append(Helper.fitString2LengthAlignRight(
						flrTaetigkeit.getC_nr(), 14, ' ')); // persnr
				sb.append(tmp).append("\r\n");

			}
			session.close();

			getTheClient(request, response).setSMsg(new String(sb));

		} else if (command.equals(TheApp.CMD_ZE_MECS_PERSSTAMM)) {
			// Personalstamm holen

			StringBuffer sb = new StringBuffer();

			// unbedingt nach personalnummer sortieren
			PersonalDto[] personalDtos = getPersonalFac()
					.personalFindByCAusweisSortiertNachPersonalnr();
			for (int i = 0; i < personalDtos.length; i++) {
				PersonalDto personalDto = personalDtos[i];
				personalDto.setPartnerDto(getPartnerFac()
						.partnerFindByPrimaryKey(personalDto.getPartnerIId(),
								theclientDto));
				StringBuffer tmp = new StringBuffer();

				tmp.setLength(0);
				tmp.append(Helper.fitString2LengthAlignRight(
						personalDto.getCPersonalnr() + "", 5, '0')); // persnr
				tmp.append(Helper.fitString2Length("", 3, ' ')); // zutrkl

				String sVorname = personalDto.getPartnerDto()
						.getCName2vornamefirmazeile2();
				String sNachname = personalDto.getPartnerDto()
						.getCName1nachnamefirmazeile1();

				if (sVorname == null) {
					sVorname = "";
				}

				tmp.append(Helper.fitString2Length(sVorname + " " + sNachname,
						25, ' ')); // name
				sb.append(tmp).append("\r\n");

			}
			getTheClient(request, response).setSMsg(new String(sb));
		} else if (command.equals(TheApp.CMD_ZE_MECS_SALDO)) {
			String ausweis = "";
			try {
				ausweis = request.getParameter("ausweis");
			} catch (Exception e) {
				getTheClient(request, response).setBResponseIsReady(true);
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Parameter 'ausweis' nicht angegeben");
				myLogger.error("doPost; Exception aufgetreten", e);
				return null;
			}
			if (ausweis.startsWith("$P")) {
				ausweis = ausweis.substring(2);
			}

			ausweis = ausweis.trim();

			ParametermandantDto parameterDto = getParameterFac()
					.getMandantparameter(mandant,
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_LEAD_IN_AUSWEISNUMMER_MECS);

			String leadIn = "";
			if (parameterDto.getCWert() != null) {
				leadIn = parameterDto.getCWert().trim();
				int iLaenge = leadIn.length();
				if (ausweis.length() > iLaenge) {
					ausweis = ausweis.substring(iLaenge);
				}
			}

			PersonalDto personalDto = getPersonalFac().personalFindByCAusweis(
					ausweis);

			personalDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
					personalDto.getPartnerIId(), theclientDto));

			java.sql.Timestamp ts = new java.sql.Timestamp(
					System.currentTimeMillis() - 3600000 * 24);
			ts = com.lp.util.Helper.cutTimestamp(ts);

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(ts.getTime());

			String urlaub = null;
			try {
				urlaub = getZeiterfassungsFac()
						.erstelleMonatsAbrechnungFuerBDE(personalDto.getIId(),
								new Integer(c.get(Calendar.YEAR)),
								new Integer(c.get(Calendar.MONTH)), false,
								new java.sql.Date(ts.getTime()), theclientDto,
								true, false);

			} catch (EJBExceptionLP ex7) {

				if (ex7.getCause() instanceof EJBExceptionLP) {
					EJBExceptionLP e = (EJBExceptionLP) ex7.getCause();
					if (e != null
							&& e.getCode() == EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM) {
						getTheClient(request, response)
								.setSMsg(
										new String(
												"FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM"));
						return getSJSPNext();

					}
				}
				getTheClient(request, response).setSMsg(
						new String(ex7.getMessage()));
				setSJSPNext("bdestation.jsp");

			}

			getTheClient(request, response).setSMsg(urlaub);

		} else if (command.equals(TheApp.CMD_ZE_MECS_ZEITBUCHEN)
				|| command.equals(TheApp.CMD_ZE_MECS_ZEITBUCHENFINGERPRINT)) {
			String record = null;

			if (command.equals(TheApp.CMD_ZE_MECS_ZEITBUCHEN)) {
				record = request.getParameter("record");
			} else {
				record = request.getParameter("recordfingerprint");

			}

			record = Helper.fitString2Length(record, 200, ' ');

			String schluesselNr = record.substring(19, 39).trim();
			String zeit = record.substring(5, 19);
			String taetigkeit = record.substring(3, 5);
			// SP753
			String terminal = record.substring(64, 86);
			// Damit die Sollzeitenueberschreitungspruefeung nicht durchgefuehrt
			// wird:
			terminal = "ZT:" + terminal;
			terminal = terminal.trim();

			boolean bAbliefern = false;

			ArtikelDto artikelDtoTaetigkeit = null;

			if (record.substring(130, 155).trim().equals("$ABLIEFERN")) {
				bAbliefern = true;
			} else {

				artikelDtoTaetigkeit = getArtikelFac().artikelFindByCNrOhneExc(
						record.substring(132, 155).trim(), theclientDto);
			}

			ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
			ZeitdatenDto zeitdatenDtoEnde = new ZeitdatenDto();
			zeitdatenDto.setCWowurdegebucht(terminal);
			zeitdatenDtoEnde.setCWowurdegebucht(terminal);

			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, new Integer(zeit.substring(0, 4)).intValue());
			c.set(Calendar.MONTH,
					new Integer(zeit.substring(4, 6)).intValue() - 1);
			c.set(Calendar.DAY_OF_MONTH,
					new Integer(zeit.substring(6, 8)).intValue());
			c.set(Calendar.HOUR_OF_DAY,
					new Integer(zeit.substring(8, 10)).intValue());
			c.set(Calendar.MINUTE,
					new Integer(zeit.substring(10, 12)).intValue());
			c.set(Calendar.SECOND,
					new Integer(zeit.substring(12, 14)).intValue());

			zeitdatenDto
					.setTZeit(new java.sql.Timestamp(c.getTime().getTime()));
			zeitdatenDtoEnde.setTZeit(new java.sql.Timestamp(c.getTime()
					.getTime() + 1000));
			zeitdatenDto.setTAendern(zeitdatenDto.getTZeit());
			zeitdatenDtoEnde.setTAendern(zeitdatenDtoEnde.getTZeit());

			// Wenn hier NullPointerException, dann kann kein Personal mit
			// Ausweisnummer gefunden werden
			Integer personalIId = null;

			if (schluesselNr.startsWith("$P")) {
				try {
					personalIId = getPersonalFac().personalFindByCAusweis(
							schluesselNr.substring(2)).getIId();
				} catch (NullPointerException ex11) {
					String msg = "Person mit Ausweis " + schluesselNr
							+ " nicht vorhanden. ORIGINAL-Request:" + record;
					myLogger.error(msg, ex11);
					response.setStatus(HttpServletResponse.SC_OK);
					return getSJSPNext();

				}

			} else {
				if (command.equals(TheApp.CMD_ZE_MECS_ZEITBUCHEN)) {
					try {
						personalIId = getPersonalFac().personalFindByCAusweis(
								schluesselNr).getIId();
					} catch (NullPointerException ex11) {
						String msg = "Person mit Ausweis " + schluesselNr
								+ " nicht vorhanden. ORIGINAL-Request:"
								+ record;
						myLogger.error(msg, ex11);

						response.setStatus(HttpServletResponse.SC_OK);
						return getSJSPNext();

					}

				} else if (command
						.equals(TheApp.CMD_ZE_MECS_ZEITBUCHENFINGERPRINT)) {
					Integer i = new Integer(schluesselNr);
					getZutrittscontrollerFac()
							.personalfingerFindByPrimaryKey(i).getPersonalIId();
					personalIId = getZutrittscontrollerFac()
							.personalfingerFindByPrimaryKey(i).getPersonalIId();
				}
			}

			zeitdatenDto.setPersonalIId(personalIId);
			zeitdatenDtoEnde.setPersonalIId(personalIId);
			zeitdatenDtoEnde.setTaetigkeitIId(getZeiterfassungsFac()
					.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_ENDE,
							theclientDto).getIId());

			// Taetigkeiten, die MECS liefert muessen in der Tabelle LP_KEYVALUE
			// uebersetzt werden (als String)
			// Bsp: MECSTERMINAL|B1|KOMMT|java.lang.String
			try {
				String sTaetigkeit = null;

				if (schluesselNr.startsWith("$P")) {
					sTaetigkeit = record.substring(110, 126);
					Integer taetigkeitIId_Ende = getZeiterfassungsFac()
							.taetigkeitFindByCNr(
									ZeiterfassungFac.TAETIGKEIT_ENDE,
									theclientDto).getIId();
					String gutStueck = record.substring(160, 172);
					String schlechtStueck = record.substring(173, 189);

					BigDecimal nGutStueck = new BigDecimal(gutStueck.trim());
					BigDecimal nSchlechtStueck = new BigDecimal(
							schlechtStueck.trim());
					Integer artikelIId = null;

					if (artikelDtoTaetigkeit == null) {
						ParametermandantDto parameterDto = getParameterFac()
								.getMandantparameter(
										mandant,
										ParameterFac.KATEGORIE_ALLGEMEIN,
										ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL);

						if (parameterDto != null
								&& parameterDto.getCWert() != null
								&& !parameterDto.getCWert().trim().equals("")) {
							try {
								artikelIId = getArtikelFac().artikelFindByCNr(
										parameterDto.getCWert(), theclientDto)
										.getIId();

							} catch (RemoteException ex2) {
								myLogger.error("Default-Arbeitszeitartikel "
										+ parameterDto.getCWert()
										+ " nicht vorhanden.", ex2);
								return getSJSPNext();
							}

						} else {
							myLogger.error("Default-Arbeitszeitartikel "
									+ parameterDto.getCWert()
									+ " nicht definiert.");
							return getSJSPNext();
						}
					} else {
						artikelIId = artikelDtoTaetigkeit.getIId();
					}

					if (sTaetigkeit.startsWith("$A")) {

						AuftragDto auftragDto = null;
						try {
							if (sTaetigkeit.startsWith("$A")) {
								auftragDto = getAuftragFac()
										.auftragFindByMandantCNrCNr(
												mandant,
												sTaetigkeit.substring(2).trim(),
												theclientDto);
							} else {
								auftragDto = getAuftragFac()
										.auftragFindByMandantCNrCNr(
												mandant,
												sTaetigkeit.substring(3).trim(),
												theclientDto);

							}
						} catch (RemoteException ex8) {
							zeitdatenDto.setCBemerkungZuBelegart("Auftrag "
									+ sTaetigkeit.substring(2).trim()
									+ " konnte nicht gefunden werden");
							zeitdatenDto.setTaetigkeitIId(taetigkeitIId_Ende);
							getZeiterfassungsFac().createZeitdaten(
									zeitdatenDto, true, true, false,
									theclientDto);

							return getSJSPNext();
						}
						// Wenn Auftragsbeginn ->
						if (sTaetigkeit.startsWith("$A")) {
							AuftragpositionDto[] auftragpositionDtos = getAuftragpositionFac()
									.auftragpositionFindByAuftrag(
											auftragDto.getIId());

							if (auftragpositionDtos.length > 0) {
								zeitdatenDto
										.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
								zeitdatenDto.setArtikelIId(artikelIId);
								zeitdatenDto
										.setIBelegartid(auftragDto.getIId());
								zeitdatenDto
										.setIBelegartpositionid(auftragpositionDtos[0]
												.getIId());
							} else {
								myLogger.error("Buchung von MECS-TERMINAL, Ausweis: "
										+ schluesselNr
										+ ", Auftrag"
										+ sTaetigkeit
										+ " hat keine Positionen.");
								return getSJSPNext();
							}
						}
					} else if (sTaetigkeit.startsWith("$EL")
							|| sTaetigkeit.startsWith("$L")) {

						com.lp.server.fertigung.service.LosDto losDto = null;
						try {
							if (sTaetigkeit.startsWith("$L")) {

								losDto = getFertigungFac()
										.losFindByCNrMandantCNr(

										sTaetigkeit.substring(2).trim(),
												mandant);

							} else {
								losDto = getFertigungFac()
										.losFindByCNrMandantCNr(

										sTaetigkeit.substring(3).trim(),
												mandant);

							}

						} catch (EJBExceptionLP ex10) {
							zeitdatenDto.setCBemerkungZuBelegart("Los "
									+ sTaetigkeit.substring(2).trim()
									+ " konnte nicht gefunden werden");
							zeitdatenDto.setTaetigkeitIId(taetigkeitIId_Ende);
							getZeiterfassungsFac().createZeitdaten(
									zeitdatenDto, true, true, false,
									theclientDto);
							return getSJSPNext();
						}

						if (bAbliefern == true) {
							LosablieferungDto losablieferungDto = new LosablieferungDto();
							losablieferungDto.setLosIId(losDto.getIId());
							String menge = record.substring(155, 170);
							BigDecimal nMenge = new BigDecimal(menge.trim());
							losablieferungDto.setNMenge(nMenge);
							losablieferungDto.setTAendern(zeitdatenDto
									.getTZeit());

							if (nMenge.doubleValue() > 0) {
								// lt. FM
								BigDecimal bdBisherErledigt = getFertigungFac()
										.getErledigteMenge(losDto.getIId(),
												theclientDto);

								if (bdBisherErledigt.add(nMenge).doubleValue() > losDto
										.getNLosgroesse().doubleValue()) {
									getFertigungFac().aendereLosgroesse(
											losDto.getIId(),
											bdBisherErledigt.add(nMenge)
													.intValue(), false,
											theclientDto);

									// SP933
									losDto.setNLosgroesse(bdBisherErledigt
											.add(nMenge));

								}

								try {
									getFertigungFac().bucheMaterialAufLos(
											losDto, nMenge, false, false, true,
											theclientDto, null, false);
								} catch (Exception e1) {
									// Terminal darf keinen Fehler bekommen
								}

								getFertigungFac()
										.createLosablieferungFuerTerminalOhnePreisberechnung(
												losablieferungDto,
												theclientDto, false);

								try {
									getFertigungFac()
											.aktualisiereNachtraeglichPreiseAllerLosablieferungen(
													losDto.getIId(),
													theclientDto, true);
								} catch (Exception e) {
									// PREISBERECHNUNG FEHLGESCHLAGEN
									myLogger.error(
											"Preisberechnung der Ablieferungen f\u00FCr Los "
													+ losDto.getCNr()
													+ " fehlgeschlagen. Bitte manuell ausfuehren",
											e);
								}
							}

							// PJ17748

							ParametermandantDto parameterAblieferungBuchtEndeDto = getParameterFac()
									.getMandantparameter(
											mandant,
											ParameterFac.KATEGORIE_FERTIGUNG,
											ParameterFac.PARAMETER_ABLIEFERUNG_BUCHT_ENDE);

							Boolean bAblieferungBuchtEndeDto = (Boolean) parameterAblieferungBuchtEndeDto
									.getCWertAsObject();

							if (bAblieferungBuchtEndeDto == true) {
								zeitdatenDto
										.setTaetigkeitIId(taetigkeitIId_Ende);
								zeitdatenDto.setCBelegartnr(null);
								zeitdatenDto.setArtikelIId(null);
								zeitdatenDto.setIBelegartid(null);
								zeitdatenDto.setIBelegartpositionid(null);
								Integer zeitdatenIId = getZeiterfassungsFac()
										.createZeitdaten(zeitdatenDto, false,
												false, false, theclientDto);

								// PJ17797
								if (nMenge.doubleValue() > 0) {
									if (getMandantFac()
											.darfAnwenderAufZusatzfunktionZugreifen(
													MandantFac.ZUSATZFUNKTION_STUECKRUECKMELDUNG,
													theclientDto)) {

										Integer lossollarbeitsplanIId = null;
										LossollarbeitsplanDto[] sollDtos = getFertigungFac()
												.lossollarbeitsplanFindByLosIId(
														losDto.getIId());
										if (sollDtos.length > 0) {
											lossollarbeitsplanIId = sollDtos[sollDtos.length - 1]
													.getIId();
										} else {
											lossollarbeitsplanIId = getFertigungFac()
													.defaultArbeitszeitartikelErstellen(
															losDto,
															theclientDto);
										}

										LosgutschlechtDto losgutschlechtDto = new LosgutschlechtDto();
										losgutschlechtDto
												.setZeitdatenIId(zeitdatenIId);
										losgutschlechtDto
												.setLossollarbeitsplanIId(lossollarbeitsplanIId);
										losgutschlechtDto.setNGut(nMenge);
										losgutschlechtDto
												.setNSchlecht(new BigDecimal(0));
										losgutschlechtDto
												.setNInarbeit(new BigDecimal(0));

										getFertigungFac()
												.createLosgutschlecht(
														losgutschlechtDto,
														theclientDto);
									}
								}

							}

							return getSJSPNext();
						}

						// Wenn Auftragsbeginn ->
						if (sTaetigkeit.startsWith("$L")) {
							zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
							zeitdatenDto.setArtikelIId(artikelIId);
							zeitdatenDto.setIBelegartid(losDto.getIId());

							LossollarbeitsplanDto[] sollDtos = getFertigungFac()
									.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
											losDto.getIId(), artikelIId);
							if (sollDtos.length > 0) {
								zeitdatenDto.setIBelegartpositionid(sollDtos[0]
										.getIId());
							}
						} else {
							// Hole letzten begonnenen Auftrag und hinterlege
							// gut/schlechtstueck
							Session session = FLRSessionFactory.getFactory()
									.openSession();
							org.hibernate.Criteria liste = session
									.createCriteria(FLRZeitdaten.class);
							liste.add(Expression
									.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
											personalIId));
							/*
							 * liste.add(Expression.eq(ZeiterfassungFac.
							 * FLR_ZEITDATEN_C_BELEGARTNR ,
							 * LocaleFac.BELEGART_LOS)); liste.add(Expression.eq
							 * (ZeiterfassungFac.FLR_ZEITDATEN_I_BELEGARTID,
							 * losDto.getIId()));
							 */
							liste.add(Expression
									.gt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
											Helper.cutTimestamp(zeitdatenDto
													.getTZeit())));

							liste.addOrder(Order
									.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
							// liste.setMaxResults(1);
							List<?> lReisezeiten = liste.list();

							Iterator<?> it = lReisezeiten.iterator();
							zeitdatenDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
							zeitdatenDto.setArtikelIId(artikelIId);
							zeitdatenDto.setIBelegartid(losDto.getIId());

							ZeitdatenDto letzterAuftrag = null;
							while (it.hasNext()) {
								FLRZeitdaten flrLetzerAuftrag = (FLRZeitdaten) it
										.next();

								if (flrLetzerAuftrag.getC_belegartnr() != null
										&& flrLetzerAuftrag.getI_belegartid() != null) {
									if (flrLetzerAuftrag.getC_belegartnr()
											.equals(zeitdatenDto
													.getCBelegartnr())
											&& flrLetzerAuftrag
													.getI_belegartid()
													.equals(zeitdatenDto
															.getIBelegartid())) {
										letzterAuftrag = getZeiterfassungsFac()
												.zeitdatenFindByPrimaryKey(
														flrLetzerAuftrag
																.getI_id(),
														theclientDto);
										break;
									}
								} else if (flrLetzerAuftrag
										.getTaetigkeit_i_id() != null
										&& flrLetzerAuftrag
												.getTaetigkeit_i_id().equals(
														taetigkeitIId_Ende)) {
									break;
								}

							}

							if (letzterAuftrag != null) {
								// Hier eintragen
								ZeitdatenDto auftragsbeginn = getZeiterfassungsFac()
										.zeitdatenFindByPrimaryKey(
												letzterAuftrag.getIId(),
												theclientDto);

								// auftragsbeginn.setNGut(nGutStueck);
								// auftragsbeginn.setNSchlecht(nSchlechtStueck);
								getZeiterfassungsFac().updateZeitdaten(
										auftragsbeginn, theclientDto);
								// und buche ENDE
								zeitdatenDto = zeitdatenDtoEnde;

							} else {
								zeitdatenDto
										.setCBelegartnr(LocaleFac.BELEGART_LOS);
								zeitdatenDto.setArtikelIId(artikelIId);
								zeitdatenDto.setIBelegartid(losDto.getIId());
								// zeitdatenDto.setNGut(nGutStueck);
								// zeitdatenDto.setNSchlecht(nSchlechtStueck);

								getZeiterfassungsFac().createZeitdaten(
										zeitdatenDto, true, true, false,
										theclientDto);
								zeitdatenDto = zeitdatenDtoEnde;
							}
							session.close();

							/*
							 * if (lReisezeiten.size() > 0) { FLRZeitdaten
							 * flrZeitdaten = (FLRZeitdaten)
							 * lReisezeiten.iterator().next();
							 * 
							 * ZeitdatenDto losbeginn = getZeiterfassungsFac().
							 * zeitdatenFindByPrimaryKey(flrZeitdaten.getI_id(),
							 * cNrUser);
							 * 
							 * losbeginn.setNGut(nGutStueck);
							 * losbeginn.setNSchlecht(nSchlechtStueck);
							 * getZeiterfassungsFac().updateZeitdaten(losbeginn,
							 * cNrUser); //und buche ENDE zeitdatenDto =
							 * zeitdatenDtoEnde; } else {
							 * zeitdatenDto.setCBelegartnr
							 * (LocaleFac.BELEGART_LOS);
							 * zeitdatenDto.setArtikelIId(artikelIId);
							 * zeitdatenDto.setIBelegartid(losDto.getIId());
							 * zeitdatenDto.setNGut(nGutStueck);
							 * zeitdatenDto.setNSchlecht(nSchlechtStueck);
							 * getZeiterfassungsFac
							 * ().createZeitdaten(zeitdatenDto, true, true,
							 * cNrUser); zeitdatenDto = zeitdatenDtoEnde; }
							 * 
							 * session.close();
							 */

						}

					} else {
						zeitdatenDto.setTaetigkeitIId(getZeiterfassungsFac()
								.taetigkeitFindByCNr(
										Helper.fitString2Length(
												sTaetigkeit.substring(1), 15,
												' '), theclientDto).getIId());
					}

				}

				else {
					sTaetigkeit = getSystemServicesFac()
							.keyvalueFindByPrimaryKey(
									SystemServicesFac.KEYVALUE_MECSTERMINAL,
									taetigkeit).getCValue();
					if (sTaetigkeit != null
							&& !sTaetigkeit
									.equals(ZeiterfassungFac.TAETIGKEIT_REISE
											.trim())) {
						zeitdatenDto.setTaetigkeitIId(getZeiterfassungsFac()
								.taetigkeitFindByCNr(
										Helper.fitString2Length(sTaetigkeit,
												15, ' '), theclientDto)
								.getIId());
					}
				}

				// Resezeiten wenn Taetigkeit REISE
				if (sTaetigkeit != null
						&& sTaetigkeit.equals(ZeiterfassungFac.TAETIGKEIT_REISE
								.trim())) {
					ReiseDto reiseDto = new ReiseDto();
					reiseDto.setPersonalIId(personalIId);

					// Letzte Reise von HEUTE holen
					// Heute 00:00 Uhr
					Calendar cTemp = Calendar.getInstance();
					cTemp.setTimeInMillis(zeitdatenDto.getTZeit().getTime());
					cTemp.set(Calendar.HOUR_OF_DAY, 0);
					cTemp.set(Calendar.MINUTE, 0);
					cTemp.set(Calendar.SECOND, 0);
					cTemp.set(Calendar.MILLISECOND, 0);

					Session sessReise = FLRSessionFactory.getFactory()
							.openSession();
					org.hibernate.Criteria reisezeiten = sessReise
							.createCriteria(FLRReise.class);
					reisezeiten.add(Expression.eq(
							ZeiterfassungFac.FLR_REISE_PERSONAL_I_ID,
							personalIId));
					reisezeiten.add(Expression.ge(
							ZeiterfassungFac.FLR_REISE_T_ZEIT, new Timestamp(
									cTemp.getTimeInMillis())));
					reisezeiten.add(Expression.lt(
							ZeiterfassungFac.FLR_REISE_T_ZEIT,
							zeitdatenDto.getTZeit()));
					reisezeiten.addOrder(Order
							.desc(ZeiterfassungFac.FLR_REISE_T_ZEIT));
					reisezeiten.setMaxResults(1);
					List<?> lReisezeiten = reisezeiten.list();

					if (lReisezeiten.size() == 0) {
						reiseDto.setBBeginn(Helper.boolean2Short(true));
					} else {
						FLRReise flrReise = (FLRReise) lReisezeiten.get(0);
						if (Helper.short2boolean(flrReise.getB_beginn()) == true) {
							reiseDto.setBBeginn(Helper.boolean2Short(false));
						} else {
							reiseDto.setBBeginn(Helper.boolean2Short(true));

						}
					}

					reiseDto.setTZeit(zeitdatenDto.getTZeit());

					Integer partnerMandant = getMandantFac()
							.mandantFindByPrimaryKey(mandant, theclientDto)
							.getPartnerIId();
					PartnerDto partnerDto = getPartnerFac()
							.partnerFindByPrimaryKey(partnerMandant,
									theclientDto);

					if (partnerDto.getLandplzortIId() == null) {
						throw new Exception("Mandant hat kein Land hinterlegt");
					}

					DiaetenDto[] dtos = getZeiterfassungsFac()
							.diaetenFindByLandIId(
									partnerDto.getLandplzortDto().getIlandID());

					if (dtos.length == 0) {
						// Einen anlegen
						DiaetenDto dto = new DiaetenDto();
						dto.setCBez(partnerDto.getLandplzortDto().getLandDto()
								.getCName());
						dto.setLandIId(partnerDto.getLandplzortDto()
								.getIlandID());
						reiseDto.setDiaetenIId(getZeiterfassungsFac()
								.createDiaeten(dto));
					} else {
						reiseDto.setDiaetenIId(dtos[0].getIId());
					}

					getZeiterfassungsFac().createReise(reiseDto, theclientDto);
					response.setStatus(HttpServletResponse.SC_OK);
					response.flushBuffer();
					getTheClient(request, response).setBResponseIsReady(true);

					return getSJSPNext();
				}
			} catch (Exception ex3) {
				ex3.printStackTrace();
				// lt. FM darf an das MECS-Terminal nur Status=200
				// zurueckgegeben werden
				response.setStatus(HttpServletResponse.SC_OK);
				return getSJSPNext();
			}

			getZeiterfassungsFac().createZeitdaten(zeitdatenDto, true, true,
					false, theclientDto);

			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			getTheClient(request, response).setBResponseIsReady(true);
		} else if (command.equals(TheApp.CMD_ZE_QUICKZE)) {

		} else if (command.equals(TheApp.CMD_ZE_RECHNERSTART1)) {
			int i = 0;
		} else if (command.equals(TheApp.CMD_ZE_QUICKZEITERFASSUNG)) {
			if (getTheClient(request, response).getSMsg() == null) {
				getTheClient(request, response).setSMsg("");
			}

			String username = getCookieValue("anmeldename", request);
			String password = getCookieValue("pass", request);

			if (localeCookie != null && localeCookie.length() > 3) {
				localeLogon = new Locale(localeCookie.substring(0, 2),
						localeCookie.substring(2, 4));
			}

			if (username == null || password == null) {
				response.sendError(
						HttpServletResponse.SC_BAD_REQUEST,
						"Es wurde kein Benutzername oder Kennwort angegeben. Bitte verwenden Sie http://?????cmd=quickze");

			}
			try {
				theclientDto = getLogonFac().logon(
						Helper.getFullUsername(username),
						Helper.getMD5Hash((username + password).toCharArray()),
						localeLogon, null, null,
						new Timestamp(System.currentTimeMillis()));
			} catch (EJBExceptionLP ex12) {

				int code = ex12.getCode();
				if (code == EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY) {
					response.sendError(
							HttpServletResponse.SC_BAD_REQUEST,
							"Benutzername '"
									+ username
									+ "' konnte im System nicht gefunden werden");
				} else if (code == EJBExceptionLP.FEHLER_FALSCHES_KENNWORT) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Kennwort f\u00FCr Benutzername '" + username
									+ "' ist falsch.");
				} else if (code == EJBExceptionLP.FEHLER_BENUTZER_IST_GESPERRT) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Benutzername '" + username + "' ist gesperrt.");
				} else if (code == EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Benutzername '" + username
									+ "' ist nicht mehr g\u00FCltig.");
				} else if (code == EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) {
					response.sendError(
							HttpServletResponse.SC_BAD_REQUEST,
							"Benutzername '"
									+ username
									+ "' darf sich bei dem Mandanten nicht anmelden.");
				} else if (code == EJBExceptionLP.FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Kein Eintrag in Benutzermandant f\u00FCr Benutzername '"
									+ username + "'.");
				} else if (ex12.getCode() == EJBExceptionLP.FEHLER_MAXIMALE_BENUTZERANZAHL_UEBERSCHRITTEN) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Maximale Benutzeranzahl \u00FCberschritten '"
									+ username + "'.");
					return null;
				} else if (code == EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_IN_DIESER_SPRACHE_NICHT_ANMELDEN) {
					ArrayList<?> al = ((EJBExceptionLP) ex12.getCause())
							.getAlInfoForTheClient();
					String zusatz = "";
					if (al.size() > 0 && al.get(0) instanceof Locale) {
						Locale loc = (Locale) al.get(0);
						zusatz = "(" + loc.getDisplayLanguage() + "|"
								+ loc.getDisplayCountry() + ")";
					}
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Benutzer '" + username + "' darf sich in '"
									+ zusatz + "' nicht anmelden.");
				}

				return null;
			}

			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKey(theclientDto.getIDPersonal(),
							theclientDto);

			personalDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
					personalDto.getPartnerIId(), theclientDto));

			HashMap<String, Object> hmData = new HashMap<String, Object>();

			TextDto textDto = getSystemMultilanguageFac()
					.textFindByPrimaryKeyOhneExc("quickze.sondertaetigkeit",
							theclientDto.getMandant(),
							theclientDto.getLocUiAsString());
			if (textDto != null) {
				hmData.put("bezeichnung_sondertaetigkeit", textDto.getCText());
			} else {
				hmData.put("bezeichnung_sondertaetigkeit",
						"Sondert\u00E4tigkeit");
			}

			// Belegarten holen

			Map<String, String> b = getZeiterfassungsFac()
					.getBebuchbareBelegarten(theclientDto);

			hmData.put("belegarten", b);

			String firstBelegart = (String) b.keySet().iterator().next();
			String belegart = null;
			if (request.getParameter("belegart") == null) {
				belegart = firstBelegart;
			} else {
				belegart = request.getParameter("belegart");
			}

			if (belegart.equals(LocaleFac.BELEGART_AUFTRAG)) {
				textDto = getSystemMultilanguageFac()
						.textFindByPrimaryKeyOhneExc("quickze.offenerauftrag",
								theclientDto.getMandant(),
								theclientDto.getLocUiAsString());
			} else if (belegart.equals(LocaleFac.BELEGART_LOS)) {
				textDto = getSystemMultilanguageFac()
						.textFindByPrimaryKeyOhneExc("quickze.offeneslos",
								theclientDto.getMandant(),
								theclientDto.getLocUiAsString());
			} else if (belegart.equals(LocaleFac.BELEGART_ANGEBOT)) {
				textDto = getSystemMultilanguageFac()
						.textFindByPrimaryKeyOhneExc("quickze.offenesangebot",
								theclientDto.getMandant(),
								theclientDto.getLocUiAsString());
			} else if (belegart.equals(LocaleFac.BELEGART_PROJEKT)) {
				textDto = getSystemMultilanguageFac()
						.textFindByPrimaryKeyOhneExc("quickze.offenesprojekt",
								theclientDto.getMandant(),
								theclientDto.getLocUiAsString());
			}
			if (textDto != null) {
				hmData.put("bezeichnung_offenerauftrag", textDto.getCText());
			} else {
				hmData.put("bezeichnung_offenerauftrag", "Offener Beleg");
			}

			textDto = getSystemMultilanguageFac().textFindByPrimaryKeyOhneExc(
					"quickze.taetigkeit", theclientDto.getMandant(),
					theclientDto.getLocUiAsString());
			if (textDto != null) {
				hmData.put("bezeichnung_taetigkeit", textDto.getCText());
			} else {
				hmData.put("bezeichnung_taetigkeit", "T\u00E4tigkeit");
			}

			textDto = getSystemMultilanguageFac().textFindByPrimaryKeyOhneExc(
					"quickze.kunde", theclientDto.getMandant(),
					theclientDto.getLocUiAsString());
			if (textDto != null) {
				hmData.put("bezeichnung_kunde", textDto.getCText());
			} else {
				hmData.put("bezeichnung_kunde", "Kunde");
			}

			textDto = getSystemMultilanguageFac().textFindByPrimaryKeyOhneExc(
					"quickze.belegart", theclientDto.getMandant(),
					theclientDto.getLocUiAsString());
			if (textDto != null) {
				hmData.put("bezeichnung_belegart", textDto.getCText());
			} else {
				hmData.put("bezeichnung_belegart", "Belegart");
			}

			textDto = getSystemMultilanguageFac().textFindByPrimaryKeyOhneExc(
					"quickze.bemerkung", theclientDto.getMandant(),
					theclientDto.getLocUiAsString());
			if (textDto != null) {
				hmData.put("bezeichnung_bemerkung", textDto.getCText());
			} else {
				hmData.put("bezeichnung_bemerkung", "Bemerkung");
			}

			hmData.put("locale", Helper.locale2String(localeLogon).trim());
			hmData.put("mandant", mandant);
			hmData.put("person", personalDto.getPartnerDto().formatAnrede());

			// Kunden mit offenen Auftraegen holen
			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery = "";
			if (belegart.equals(LocaleFac.BELEGART_AUFTRAG)) {
				sQuery = "SELECT flrkunde.flrpartner.c_name1nachnamefirmazeile1, flrkunde.flrpartner.c_name2vornamefirmazeile2, flrkunde.flrpartner.i_id "
						+ " FROM FLRAuftrag AS auftrag WHERE (auftrag.auftragstatus_c_nr='"
						+ LocaleFac.STATUS_OFFEN
						+ "' OR auftrag.auftragstatus_c_nr='"
						+ LocaleFac.STATUS_TEILERLEDIGT
						+ "') AND auftrag.mandant_c_nr='"
						+ theclientDto.getMandant()
						+ "'"
						+ "  GROUP BY flrkunde.flrpartner.c_name1nachnamefirmazeile1, flrkunde.flrpartner.c_name2vornamefirmazeile2, flrkunde.flrpartner.i_id  ORDER BY flrkunde.flrpartner.c_name1nachnamefirmazeile1 ASC";
			} else if (belegart.equals(LocaleFac.BELEGART_ANGEBOT)) {
				sQuery = "SELECT flrkunde.flrpartner.c_name1nachnamefirmazeile1, flrkunde.flrpartner.c_name2vornamefirmazeile2, flrkunde.flrpartner.i_id "
						+ " FROM FLRAngebot AS angebot WHERE (angebot.angebotstatus_c_nr='"
						+ LocaleFac.STATUS_OFFEN
						+ "') AND angebot.mandant_c_nr='"
						+ theclientDto.getMandant()
						+ "'"
						+ "  GROUP BY flrkunde.flrpartner.c_name1nachnamefirmazeile1, flrkunde.flrpartner.c_name2vornamefirmazeile2, flrkunde.flrpartner.i_id  ORDER BY flrkunde.flrpartner.c_name1nachnamefirmazeile1 ASC";
			} else if (belegart.equals(LocaleFac.BELEGART_PROJEKT)) {
				sQuery = "SELECT flrpartner.c_name1nachnamefirmazeile1, flrpartner.c_name2vornamefirmazeile2, flrpartner.i_id "
						+ " FROM FLRProjekt AS projekt WHERE projekt.status_c_nr<>'"
						+ ProjektServiceFac.PROJEKT_STATUS_STORNIERT
						+ "' AND projekt.t_erledigungsdatum IS NULL AND projekt.mandant_c_nr='"
						+ theclientDto.getMandant()
						+ "'"
						+ "  GROUP BY flrpartner.c_name1nachnamefirmazeile1, flrpartner.c_name2vornamefirmazeile2, flrpartner.i_id  ORDER BY flrpartner.c_name1nachnamefirmazeile1 ASC";

			}
			LinkedHashMap<Object, Object> tmKunden = new LinkedHashMap<Object, Object>();
			Integer firstKunde = null;
			if (!belegart.equals(LocaleFac.BELEGART_LOS)) {
				Query kunden = session.createQuery(sQuery);

				List<?> resultList = kunden.list();

				Iterator<?> resultListIterator = resultList.iterator();

				int row = 0;

				while (resultListIterator.hasNext()) {
					Object o[] = (Object[]) resultListIterator.next();
					if (row == 0) {
						firstKunde = (Integer) o[2];
					}
					if (o[1] == null) {
						tmKunden.put(o[2], o[0]);

					} else {
						tmKunden.put(o[2], o[0] + " " + o[1]);

					}
					row++;
				}
				session.close();
			} else {

				sQuery = "SELECT los "
						+ " FROM FLRLosReport AS los WHERE (los.status_c_nr='"
						+ LocaleFac.STATUS_AUSGEGEBEN
						+ "' OR los.status_c_nr='"
						+ LocaleFac.STATUS_IN_PRODUKTION
						+ "' OR los.status_c_nr='"
						+ LocaleFac.STATUS_TEILERLEDIGT
						+ "')  AND los.mandant_c_nr='"
						+ theclientDto.getMandant()
						+ "' AND ( los.flrauftrag IS NOT NULL OR los.flrkunde IS NOT NULL) ";

				Query kunden = session.createQuery(sQuery);

				List<?> resultList = kunden.list();

				Iterator<?> resultListIterator = resultList.iterator();

				int row = 0;

				while (resultListIterator.hasNext()) {
					FLRLosReport los = (FLRLosReport) resultListIterator.next();

					Integer partnerIId = null;
					String kundenname = "";
					if (los.getFlrauftrag() != null) {
						partnerIId = los.getFlrauftrag().getFlrkunde()
								.getFlrpartner().getI_id();

						kundenname += los.getFlrauftrag().getFlrkunde()
								.getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						if (los.getFlrauftrag().getFlrkunde().getFlrpartner()
								.getC_name2vornamefirmazeile2() != null) {
							kundenname += " "
									+ los.getFlrauftrag().getFlrkunde()
											.getFlrpartner()
											.getC_name2vornamefirmazeile2();
						}

					} else {
						partnerIId = los.getFlrkunde().getFlrpartner()
								.getI_id();
						kundenname += los.getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

						if (los.getFlrkunde().getFlrpartner()
								.getC_name2vornamefirmazeile2() != null) {
							kundenname += " "
									+ los.getFlrkunde().getFlrpartner()
											.getC_name2vornamefirmazeile2();
						}
					}

					if (row == 0) {
						firstKunde = partnerIId;
					}
					if (!tmKunden.containsKey(partnerIId)) {

						tmKunden.put(partnerIId, kundenname);
					}

					row++;
				}

				tmKunden = (LinkedHashMap) Helper.sortByValue(tmKunden);

				// leeren Kunden einfuegen
				tmKunden.put("", "--KEIN--");

				session.close();
			}
			hmData.put("kunden", tmKunden);

			// Sondertaetigkeiten holen

			Map<Integer, String> m = getZeiterfassungsFac()
					.getAllSprSondertaetigkeitenNurBDEBuchbar(
							theclientDto.getLocUiAsString());

			hmData.put("taetigkeiten", m);

			// Kunden holen

			Integer kunde = null;
			if (request.getParameter("kunde") == null) {
				kunde = firstKunde;
			} else {
				if (!request.getParameter("kunde").equals("")) {
					if (!request.getParameter("kunde").equals(" ")) {
						if (!request.getParameter("kunde").trim()
								.equals("null")) {
							kunde = new Integer(request.getParameter("kunde")
									.trim());
						}
					}

				}
			}

			hmData.put("selectedbelegart", belegart);

			session = FLRSessionFactory.getFactory().openSession();

			if (belegart.equals(LocaleFac.BELEGART_AUFTRAG)) {

				sQuery = "SELECT auftrag.i_id, auftrag.c_nr, auftrag.c_bez, auftrag.t_liefertermin, auftrag.flrkunde.flrpartner.i_id "
						+ " FROM FLRAuftrag AS auftrag WHERE (auftrag.auftragstatus_c_nr='"
						+ LocaleFac.STATUS_OFFEN
						+ "' OR auftrag.auftragstatus_c_nr='"
						+ LocaleFac.STATUS_TEILERLEDIGT
						+ "') AND auftrag.flrkunde.flrpartner.i_id="
						+ kunde
						+ " AND auftrag.b_versteckt=0 ORDER BY auftrag.c_nr ASC";
			} else if (belegart.equals(LocaleFac.BELEGART_LOS)) {
				sQuery = "SELECT los.i_id, los.c_nr, los.c_projekt, los.t_produktionsende, coalesce(auftragpartner.i_id,kundepartner.i_id) "
						+ " FROM FLRLosReport AS los LEFT OUTER JOIN los.flrauftrag.flrkunde.flrpartner as auftragpartner LEFT OUTER JOIN los.flrkunde.flrpartner as kundepartner  WHERE (los.status_c_nr='"
						+ LocaleFac.STATUS_AUSGEGEBEN
						+ "' OR los.status_c_nr='"
						+ LocaleFac.STATUS_IN_PRODUKTION
						+ "' OR los.status_c_nr='"
						+ LocaleFac.STATUS_TEILERLEDIGT + "') ";
				if (kunde != null) {
					sQuery += " AND ( auftragpartner.i_id=" + kunde
							+ " OR kundepartner.i_id=" + kunde + ")";

				} else {
					sQuery += " AND ( auftragpartner.i_id IS NULL AND kundepartner.i_id IS NULL)";
				}
				sQuery += " ORDER BY los.c_nr ASC";
			} else if (belegart.equals(LocaleFac.BELEGART_ANGEBOT)) {
				sQuery = "SELECT angebot.i_id, angebot.c_nr, angebot.c_bez, angebot.t_realisierungstermin, angebot.flrkunde.flrpartner.i_id "
						+ " FROM FLRAngebot AS angebot WHERE angebot.angebotstatus_c_nr='"
						+ LocaleFac.STATUS_OFFEN
						+ "' AND angebot.flrkunde.flrpartner.i_id="
						+ kunde
						+ "  ORDER BY angebot.c_nr ASC";
			} else if (belegart.equals(LocaleFac.BELEGART_PROJEKT)) {
				sQuery = "SELECT projekt.i_id, projekt.c_nr, projekt.c_titel, projekt.t_zielwunschdatum, projekt.partner_i_id "
						+ " FROM FLRProjekt AS projekt WHERE projekt.status_c_nr<>'"
						+ ProjektServiceFac.PROJEKT_STATUS_STORNIERT
						+ "' AND projekt.t_erledigungsdatum IS NULL AND projekt.partner_i_id="
						+ kunde + " ORDER BY projekt.c_nr ASC";
			}
			Query auftraege = session.createQuery(sQuery);

			List<?> resultList = auftraege.list();

			Iterator resultListIterator = resultList.iterator();
			LinkedHashMap<Object, Object> tmAuftraege = new LinkedHashMap<Object, Object>();
			Object partnerIId = null;
			String selectedAuftragId = null;
			while (resultListIterator.hasNext()) {
				Object o[] = (Object[]) resultListIterator.next();
				partnerIId = (Integer) o[4];

				if (o[2] == null) {
					tmAuftraege.put(o[0], o[1]);

				} else {
					tmAuftraege.put(o[0], o[1] + " " + o[2]);

				}
				if (selectedAuftragId == null) {
					selectedAuftragId = o[0].toString();
				}
			}

			session.close();
			hmData.put("auftraege", tmAuftraege);

			hmData.put("selectedkunde", partnerIId);

			if (request.getParameter("auftrag") != null
					&& request.getParameter("auftrag").length() > 0) {
				selectedAuftragId = request.getParameter("auftrag");

			}

			// Artikel zu Auftrag holen
			session = FLRSessionFactory.getFactory().openSession();

			if (belegart.equals(LocaleFac.BELEGART_AUFTRAG)) {

				sQuery = "SELECT a.i_id, a.flrartikel.i_id FROM FLRAuftragposition AS a WHERE a.flrauftrag.i_id="
						+ selectedAuftragId
						+ " AND a.flrartikel.artikelart_c_nr='"
						+ ArtikelFac.ARTIKELART_ARBEITSZEIT + "'";
			} else if (belegart.equals(LocaleFac.BELEGART_LOS)) {

				sQuery = "SELECT a.i_id, a.flrartikel.i_id FROM FLRLossollarbeitsplan AS a WHERE a.los_i_id="
						+ selectedAuftragId
						+ " AND a.flrartikel.artikelart_c_nr='"
						+ ArtikelFac.ARTIKELART_ARBEITSZEIT + "'";
			} else if (belegart.equals(LocaleFac.BELEGART_ANGEBOT)) {

				sQuery = "SELECT a.i_id, a.flrartikel.i_id FROM FLRAngebotposition AS a WHERE a.flrangebot.i_id="
						+ selectedAuftragId
						+ " AND a.flrartikel.artikelart_c_nr='"
						+ ArtikelFac.ARTIKELART_ARBEITSZEIT + "'";
			}
			LinkedHashMap<Object, Object> tmArtikel = new LinkedHashMap<Object, Object>();
			if (!belegart.equals(LocaleFac.BELEGART_PROJEKT)) {
				Query artikelListe = session.createQuery(sQuery);

				resultList = artikelListe.list();

				resultListIterator = resultList.iterator();

				if (resultList.size() > 0) {
					tmArtikel.put(-1, " - - - - - - Beleg - - - - - -");
				}

				while (resultListIterator.hasNext()) {
					Object[] zeile = (Object[]) resultListIterator.next();

					Integer artikelIId = (Integer) zeile[1];

					String sollIst = "";

					if (belegart.equals(LocaleFac.BELEGART_AUFTRAG)) {

						BigDecimal bdSoll = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(
										(Integer) zeile[0]).getNMenge();
						sollIst = "; Soll: "
								+ Helper.formatZahl(bdSoll, 2,
										theclientDto.getLocUi());

						Double dIst;
						try {

							boolean bZuvieleZeitbuchungen = getZeiterfassungsFac()
									.sindZuvieleZeitdatenEinesBelegesVorhanden(
											belegart, new Integer(selectedAuftragId),
											theclientDto);
							if (bZuvieleZeitbuchungen == false) {

								dIst = getZeiterfassungsFac()
										.getSummeZeitenEinesBeleges(belegart,
												new Integer(selectedAuftragId),
												(Integer) zeile[0], null, null,
												null, theclientDto);
								sollIst += " Ist: "
										+ Helper.formatZahl(dIst, 2,
												theclientDto.getLocUi());
							}
						} catch (Exception e) {
							sollIst += " Ist: ERR";
						}

					}

					String artikel = getArtikelFac().artikelFindByPrimaryKey(
							artikelIId, theclientDto)
							.formatArtikelbezeichnung()
							+ sollIst;
					if (!tmArtikel.containsKey(artikelIId)) {
						tmArtikel.put(artikelIId, artikel);

					}
				}
				session.close();
			}
			// Artikel des Auftrags + Artikel aus Personalverfuegbarkeit holen

			if (selectedAuftragId != null) {

				PersonalverfuegbarkeitDto[] personalverfuegbarkeitDtos = getPersonalFac()
						.personalverfuegbarkeitFindByPersonalIId(
								personalDto.getIId());
				if (personalverfuegbarkeitDtos.length > 0) {
					tmArtikel.put(-2, " - - - Verf\u00FCgbarkeit - - - ");
				}
				for (int i = 0; i < personalverfuegbarkeitDtos.length; i++) {
					PersonalverfuegbarkeitDto v = personalverfuegbarkeitDtos[i];
					String artikel = getArtikelFac().artikelFindByPrimaryKey(
							v.getArtikelIId(), theclientDto)
							.formatArtikelbezeichnung();
					tmArtikel.put(v.getArtikelIId(), artikel);
				}

				hmData.put("selectedauftrag", selectedAuftragId);

			}

			ParametermandantDto parameterDtoDefaultarbeitszeit = getParameterFac()
					.getMandantparameter(mandant,
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_DEFAULT_ARBEITSZEITARTIKEL);

			if (parameterDtoDefaultarbeitszeit != null
					&& parameterDtoDefaultarbeitszeit.getCWert() != null
					&& !parameterDtoDefaultarbeitszeit.getCWert().trim()
							.equals("")) {

				ArtikelDto artikelDtoDefaultArbeiztszeit = getArtikelFac()
						.artikelFindByCNr(
								parameterDtoDefaultarbeitszeit.getCWert(),
								theclientDto);
				tmArtikel.put(-3, " - - - Default-Arbeitszeitartikel - - -");
				tmArtikel.put(artikelDtoDefaultArbeiztszeit.getIId(),
						artikelDtoDefaultArbeiztszeit
								.formatArtikelbezeichnung());
			}

			hmData.put("artikel", tmArtikel);

			// Zeitbuchen
			String bucheauftrag = request.getParameter("bucheauftrag");
			String buchesondertaetigkeit = request
					.getParameter("buchesondertaetigkeit");

			// Zeit buchen
			ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
			zeitdatenDto.setPersonalIId(personalDto.getIId());
			Timestamp tZeit = new Timestamp(System.currentTimeMillis());
			zeitdatenDto.setCWowurdegebucht("Quick-ZE "
					+ request.getRemoteHost());
			String meldung = "";

			zeitdatenDto.setTZeit(tZeit);
			String bemerkung = request.getParameter("bemerkung");
			zeitdatenDto.setCBemerkungZuBelegart(bemerkung);

			if (bucheauftrag != null && bucheauftrag.length() > 0) {

				if (request.getParameter("artikel") != null) {

					Integer artikelId = new Integer(
							request.getParameter("artikel"));
					if (artikelId > 0) {

						Integer auftragIId = new Integer(
								selectedAuftragId.trim());

						String s = "Auf ";

						if (kunde != null) {
							PartnerDto partnerDto = getPartnerFac()
									.partnerFindByPrimaryKey(kunde,
											theclientDto);
							s += partnerDto.formatFixName1Name2() + ", ";
						}

						if (belegart.equals(LocaleFac.BELEGART_AUFTRAG)) {
							textDto = getSystemMultilanguageFac()
									.textFindByPrimaryKeyOhneExc(
											"quickze.auftrag",
											theclientDto.getMandant(),
											theclientDto.getLocUiAsString());

							if (textDto != null) {
								s += textDto.getCText() + " ";
							} else {
								s += "Auftrag ";
							}

							com.lp.server.auftrag.service.AuftragDto auftragDto = getAuftragFac()
									.auftragFindByPrimaryKey(auftragIId);
							s += auftragDto.getCNr();
							if (auftragDto.getCBezProjektbezeichnung() != null) {
								s += " "
										+ auftragDto
												.getCBezProjektbezeichnung();
							}

							com.lp.server.auftrag.service.AuftragpositionDto[] auftragpositionDtos = getAuftragpositionFac()
									.auftragpositionFindByAuftrag(auftragIId);
							if (auftragpositionDtos.length > 0) {
								zeitdatenDto
										.setIBelegartpositionid(auftragpositionDtos[0]
												.getIId());
							}
						} else if (belegart.equals(LocaleFac.BELEGART_ANGEBOT)) {
							textDto = getSystemMultilanguageFac()
									.textFindByPrimaryKeyOhneExc(
											"angb.angebot",
											theclientDto.getMandant(),
											theclientDto.getLocUiAsString());

							if (textDto != null) {
								s += textDto.getCText() + " ";
							} else {
								s += "Angebot ";
							}

							com.lp.server.angebot.service.AngebotDto auftragDto = getAngebotFac()
									.angebotFindByPrimaryKey(auftragIId,
											theclientDto);
							s += auftragDto.getCNr();
							if (auftragDto.getCBez() != null) {
								s += " " + auftragDto.getCBez();
							}

						} else if (belegart.equals(LocaleFac.BELEGART_PROJEKT)) {
							textDto = getSystemMultilanguageFac()
									.textFindByPrimaryKeyOhneExc(
											"lp.projekt.modulname",
											theclientDto.getMandant(),
											theclientDto.getLocUiAsString());

							if (textDto != null) {
								s += textDto.getCText() + " ";
							} else {
								s += "Projekt ";
							}

							com.lp.server.projekt.service.ProjektDto auftragDto = getProjektFac()
									.projektFindByPrimaryKey(auftragIId);
							s += auftragDto.getCNr();
							if (auftragDto.getCTitel() != null) {
								s += " " + auftragDto.getCTitel();
							}

						} else if (belegart.equals(LocaleFac.BELEGART_LOS)) {
							textDto = getSystemMultilanguageFac()
									.textFindByPrimaryKeyOhneExc(
											"fert.tab.unten.los.title",
											theclientDto.getMandant(),
											theclientDto.getLocUiAsString());

							if (textDto != null) {
								s += textDto.getCText() + " ";
							} else {
								s += "Los ";
							}

							LosDto auftragDto = getFertigungFac()
									.losFindByPrimaryKey(auftragIId);
							s += auftragDto.getCNr();
							if (auftragDto.getCProjekt() != null) {
								s += " " + auftragDto.getCProjekt();
							}

							LossollarbeitsplanDto[] dtos = getFertigungFac()
									.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(
											auftragIId, artikelId);
							if (dtos.length > 0) {
								zeitdatenDto.setIBelegartpositionid(dtos[0]
										.getIId());
							}

						}
						zeitdatenDto.setPersonalIId(personalDto.getIId());
						zeitdatenDto.setCBelegartnr(belegart);
						zeitdatenDto.setIBelegartid(auftragIId);
						zeitdatenDto.setArtikelIId(artikelId);

						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(artikelId,
										theclientDto);

						meldung += s + ", "
								+ artikelDto.formatArtikelbezeichnung();

						getZeiterfassungsFac().createZeitdaten(zeitdatenDto,
								true, true, false, theclientDto);
						meldung += " um "
								+ Helper.formatTime(tZeit, localeLogon)
								+ " gebucht.";
						getTheClient(request, response).setSMsg(meldung);
					}
				} else {

					getTheClient(request, response).setSMsg(
							"Keine Auftragsposition ausgew\u00E4hlt");
				}
			} else if (buchesondertaetigkeit != null
					&& buchesondertaetigkeit.length() > 0) {
				String zusatz = request.getParameter("zusatz");

				if (zusatz != null && zusatz.length() > 0) {
					// Zeit ist immer jetzt
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(zeitdatenDto.getTZeit().getTime());
					ZeitdatenDto[] letzeBuchungen = getZeiterfassungsFac()
							.zeitdatenFindZeitdatenEinesTagesUndEinerPersonOnheBelegzeiten(
									zeitdatenDto.getPersonalIId(),
									Helper.cutTimestamp(zeitdatenDto.getTZeit()),
									zeitdatenDto.getTZeit());
					Integer taetigkeitIId_Kommt = getZeiterfassungsFac()
							.taetigkeitFindByCNr(
									ZeiterfassungFac.TAETIGKEIT_KOMMT,
									theclientDto).getIId();
					Integer taetigkeitIId_Unter = getZeiterfassungsFac()
							.taetigkeitFindByCNr(
									ZeiterfassungFac.TAETIGKEIT_UNTER,
									theclientDto).getIId();
					Integer taetigkeitIId_Geht = getZeiterfassungsFac()
							.taetigkeitFindByCNr(
									ZeiterfassungFac.TAETIGKEIT_GEHT,
									theclientDto).getIId();
					if (zusatz.equals("spezialkommt")) {

						if (letzeBuchungen.length == 0) {
							// Zuerst Kommt und dann UNTER
							ZeitdatenDto dtoKommt = new ZeitdatenDto();
							dtoKommt.setTaetigkeitIId(taetigkeitIId_Kommt);
							dtoKommt.setPersonalIId(zeitdatenDto
									.getPersonalIId());
							dtoKommt.setCWowurdegebucht("Spezial-Kommt");
							// Zeit 100 MS vorher
							dtoKommt.setTZeit(new Timestamp(zeitdatenDto
									.getTZeit().getTime()));
							getZeiterfassungsFac().createZeitdaten(dtoKommt,
									false, false, false, theclientDto);
							// Taetigkeit GEHT Buchen
							ZeitdatenDto dtoUnter = new ZeitdatenDto();
							dtoUnter.setTaetigkeitIId(taetigkeitIId_Unter);
							dtoUnter.setPersonalIId(zeitdatenDto
									.getPersonalIId());
							dtoUnter.setCWowurdegebucht("Spezial-Kommt");
							// Zeit 100 MS nachher
							dtoUnter.setTZeit(new Timestamp(zeitdatenDto
									.getTZeit().getTime() + 96));
							getZeiterfassungsFac().createZeitdaten(dtoUnter,
									false, false, false, theclientDto);
						} else if (letzeBuchungen.length == 1) {
							Integer letztetaetigkeit = letzeBuchungen[0]
									.getTaetigkeitIId();
							// Wenn nur Kommt, dann Unter buchen
							if (taetigkeitIId_Kommt.equals(letztetaetigkeit)) {
								// Taetigkeit UNTER Buchen
								ZeitdatenDto dtoUnter = new ZeitdatenDto();
								dtoUnter.setTaetigkeitIId(taetigkeitIId_Unter);
								dtoUnter.setPersonalIId(zeitdatenDto
										.getPersonalIId());
								dtoUnter.setCWowurdegebucht("Spezial-Kommt");
								dtoUnter.setTZeit(new Timestamp(zeitdatenDto
										.getTZeit().getTime()));
								getZeiterfassungsFac().createZeitdaten(
										dtoUnter, false, false, false,
										theclientDto);

							}
						} else if (letzeBuchungen.length > 1) {
							Integer letztetaetigkeit = letzeBuchungen[letzeBuchungen.length - 1]
									.getTaetigkeitIId();
							if (taetigkeitIId_Kommt.equals(letztetaetigkeit)) {
								// Taetigkeit UNTER Buchen
								ZeitdatenDto dtoUnter = new ZeitdatenDto();
								dtoUnter.setTaetigkeitIId(taetigkeitIId_Unter);
								dtoUnter.setPersonalIId(zeitdatenDto
										.getPersonalIId());
								dtoUnter.setCWowurdegebucht("Spezial-Kommt");
								dtoUnter.setTZeit(new Timestamp(zeitdatenDto
										.getTZeit().getTime()));
								getZeiterfassungsFac().createZeitdaten(
										dtoUnter, false, false, false,
										theclientDto);

							} else {

								// Wenn letzte Taetigkeit ein Geht ist wird
								// Kommt
								// und Unter gebucht
								if (!taetigkeitIId_Geht
										.equals(letztetaetigkeit)) {

									int iSondertaetigkeitenHintereinander = 1;
									for (int i = letzeBuchungen.length - 2; i >= 0; i--) {
										ZeitdatenDto dto = letzeBuchungen[i];
										if (letztetaetigkeit.equals(dto
												.getTaetigkeitIId())) {
											iSondertaetigkeitenHintereinander++;
										} else {
											break;
										}
										letztetaetigkeit = dto
												.getTaetigkeitIId();
									}

									if (iSondertaetigkeitenHintereinander % 2 == 0) {
										// Taetigkeit UNTER Buchen
										ZeitdatenDto dtoUnter = new ZeitdatenDto();
										dtoUnter.setTaetigkeitIId(taetigkeitIId_Unter);
										dtoUnter.setPersonalIId(zeitdatenDto
												.getPersonalIId());
										dtoUnter.setCWowurdegebucht("Spezial-Geht");
										dtoUnter.setTZeit(new Timestamp(
												zeitdatenDto.getTZeit()
														.getTime()));
										getZeiterfassungsFac().createZeitdaten(
												dtoUnter, false, false, false,
												theclientDto);

										/**
										 * @todo 100ms vorher Projekt-ENDE
										 *       buchen
										 */
									}
								} else {
									// Taetigkeit KOMMT Buchen
									ZeitdatenDto dtoKommt = new ZeitdatenDto();
									dtoKommt.setTaetigkeitIId(taetigkeitIId_Kommt);
									dtoKommt.setPersonalIId(zeitdatenDto
											.getPersonalIId());
									dtoKommt.setCWowurdegebucht("Spezial-Kommt");
									dtoKommt.setTZeit(new Timestamp(
											zeitdatenDto.getTZeit().getTime()));
									getZeiterfassungsFac().createZeitdaten(
											dtoKommt, false, false, false,
											theclientDto);
									// Taetigkeit UNTER Buchen
									ZeitdatenDto dtoUnter = new ZeitdatenDto();
									dtoUnter.setTaetigkeitIId(taetigkeitIId_Unter);
									dtoUnter.setPersonalIId(zeitdatenDto
											.getPersonalIId());
									dtoUnter.setCWowurdegebucht("Spezial-Kommt");
									// Zeit 100 MS nachher
									dtoUnter.setTZeit(new Timestamp(
											zeitdatenDto.getTZeit().getTime() + 96));
									getZeiterfassungsFac().createZeitdaten(
											dtoUnter, false, false, false,
											theclientDto);

								}
							}
						}

					} else if (zusatz.equals("spezialgeht")) {
						if (letzeBuchungen.length > 1) {

							Integer letztetaetigkeit = letzeBuchungen[letzeBuchungen.length - 1]
									.getTaetigkeitIId();
							// Wenn letzte Taetigkeit kein geht ist, sonst wird
							// geht verschmissen
							if (!taetigkeitIId_Geht.equals(letztetaetigkeit)) {

								int iSondertaetigkeitenHintereinander = 1;
								for (int i = letzeBuchungen.length - 2; i >= 0; i--) {
									ZeitdatenDto dto = letzeBuchungen[i];
									if (letztetaetigkeit.equals(dto
											.getTaetigkeitIId())) {
										iSondertaetigkeitenHintereinander++;
									} else {
										break;
									}
									letztetaetigkeit = dto.getTaetigkeitIId();
								}

								if (iSondertaetigkeitenHintereinander % 2 == 1) {
									// Sondertaetigkeit Ende Buchen
									ZeitdatenDto dtoSonderEnde = new ZeitdatenDto();
									dtoSonderEnde
											.setTaetigkeitIId(letztetaetigkeit);
									dtoSonderEnde.setPersonalIId(zeitdatenDto
											.getPersonalIId());
									dtoSonderEnde
											.setCWowurdegebucht("Spezial-Geht");
									// Zeit 100 MS vorher
									dtoSonderEnde
											.setTZeit(new Timestamp(
													zeitdatenDto.getTZeit()
															.getTime() - 96));
									getZeiterfassungsFac().createZeitdaten(
											dtoSonderEnde, false, false, false,
											theclientDto);
									// Taetigkeit GEHT Buchen
									ZeitdatenDto dtoUnter = new ZeitdatenDto();
									dtoUnter.setTaetigkeitIId(taetigkeitIId_Geht);
									dtoUnter.setPersonalIId(zeitdatenDto
											.getPersonalIId());
									dtoUnter.setCWowurdegebucht("Spezial-Geht");
									// Zeit 100 MS vorher
									dtoUnter.setTZeit(new Timestamp(
											zeitdatenDto.getTZeit().getTime()));
									getZeiterfassungsFac().createZeitdaten(
											dtoUnter, false, false, false,
											theclientDto);

								} else {
									// Taetigkeit GEHT Buchen
									ZeitdatenDto dtoUnter = new ZeitdatenDto();
									dtoUnter.setTaetigkeitIId(taetigkeitIId_Geht);
									dtoUnter.setPersonalIId(zeitdatenDto
											.getPersonalIId());
									dtoUnter.setCWowurdegebucht("Spezial-Geht");
									// Zeit 100 MS vorher
									dtoUnter.setTZeit(new Timestamp(
											zeitdatenDto.getTZeit().getTime()));
									getZeiterfassungsFac().createZeitdaten(
											dtoUnter, false, false, false,
											theclientDto);

								}
							}

						}

					} else {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST,
								"zusatz '" + zusatz + "' unbekannt");
					}
					setSJSPNext("mecs.jsp");
					return getSJSPNext();
				} else {

					if (request.getParameter("taetigkeit") != null) {

						Integer taetigkeitId = new Integer(
								request.getParameter("taetigkeit"));
						zeitdatenDto.setTaetigkeitIId(taetigkeitId);

						TaetigkeitDto dto = getZeiterfassungsFac()
								.taetigkeitFindByPrimaryKey(taetigkeitId,
										theclientDto);
						meldung += dto.getBezeichnung();

						getZeiterfassungsFac().createZeitdaten(zeitdatenDto,
								true, true, false, theclientDto);
						meldung += " um "
								+ Helper.formatTime(tZeit, localeLogon)
								+ " gebucht.";
						getTheClient(request, response).setSMsg(meldung);
					} else {
						getTheClient(request, response).setSMsg(
								"Keine T\u00E4tigkeit ausgew\u00E4hlt");
					}
				}
			}
			getTheClient(request, response).setData(hmData);
			// AD+CK logout wegen usercount
			synchronized (mutex) { // PJ 15986
				getLogonFac().logout(theclientDto);
			}
		} else if (command.equals(TheApp.CMD_ZU_MECS_TERMINAL)) {
			// Personalstamm holen

			String master = request.getParameter("master");

			try {
				ZutrittscontrollerDto zutrittscontrollerDto = getZutrittscontrollerFac()
						.zutrittscontrollerFindByCNr(master);
				ZutrittsobjektDto[] zutrittsobjektDtos = getZutrittscontrollerFac()
						.zutrittsobjektFindByZutrittscontrollerIId(
								zutrittscontrollerDto.getIId());

				StringBuffer objekte = new StringBuffer();

				for (int i = 0; i < zutrittsobjektDtos.length; i++) {
					objekte.append(Helper.fitString2Length(
							zutrittsobjektDtos[i].getCNr(), 6, ' ')); // terminal-
					// id
					objekte.append(Helper.fitString2Length(
							zutrittsobjektDtos[i].getCAdresse(), 100, ' ')); // adresse
					objekte.append("\r\n");
				}
				myLogger.info(command + ":" + new String(objekte));

				getTheClient(request, response).setSMsg(new String(objekte));
			}

			catch (RemoteException ex5) {
				if (ex5.getCause() instanceof EJBExceptionLP) {
					EJBExceptionLP lpex = (EJBExceptionLP) ex5.getCause();
					if (lpex.getCode() == EJBExceptionLP.FEHLER_BEI_FIND) {
						myLogger.error("Zutrittscontroller '" + master
								+ "' nicht angelegt", ex5);
					} else {
						myLogger.error(ex5.getMessage(), ex5);
					}
				}

			}
		} else if (command.equals(TheApp.CMD_ZU_MECS_RELAIS)) {
			// Personalstamm holen

			String termid = request.getParameter("termid");
			if (termid == null || termid.length() == 0) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Es ist der Parameter termid='LanPortName' erforderlich");
				return null;
			}
			try {
				ZutrittsobjektDto zutrittsobjektDto = getZutrittscontrollerFac()
						.zutrittsobjektFindByCNr(termid);

				StringBuffer objekte = new StringBuffer();

				objekte.append("10"); // readerid
				objekte.append("0"); // port
				objekte.append(zutrittsobjektDto.getCRelais()); // relais
				String oeffnungszeit = zutrittsobjektDto.getFOeffnungszeit()
						.toString();
				oeffnungszeit = oeffnungszeit.replaceAll(",", ".");
				objekte.append(Helper.fitString2LengthAlignRight(oeffnungszeit,
						4, ' ')); // oeffnungszeit
				objekte.append(zutrittsobjektDto.getZutrittsleserCNr().trim()); // readerid
				objekte.append("\r\n");
				myLogger.info(command + ":" + new String(objekte));

				getTheClient(request, response).setSMsg(new String(objekte));
			} catch (EJBExceptionLP ex4) {

				if (ex4.getCode() == EJBExceptionLP.FEHLER_BEI_FIND) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND,
							"Zutrittsobjekt '" + termid + "' nicht angelegt");
					return null;
				} else {
					ex4.printStackTrace();
					myLogger.error(ex4.getMessage(), ex4);
				}

			}
		} else if (command.equals(TheApp.CMD_ZU_MECS_ZUTRITT)) {
			String termid = request.getParameter("termid");
			try {
				ZutrittsobjektDto dto = getZutrittscontrollerFac()
						.zutrittsobjektFindByCNr(termid);

				String s = getZutrittscontrollerFac()
						.getZutrittsdatenFuerEinObjektFuerMecs(dto.getIId(),
								theclientDto);
				myLogger.info(command + ":" + new String(s));

				getTheClient(request, response).setSMsg(new String(s));
			} catch (EJBExceptionLP ex4) {
				if (ex4.getCode() == EJBExceptionLP.FEHLER_BEI_FIND) {
					myLogger.error("Zutrittsobjekt '" + termid
							+ "' nicht angelegt", ex4);
					response.sendError(HttpServletResponse.SC_NOT_FOUND,
							"Zutrittsobjekt '" + termid + "' nicht angelegt");

				} else {
					myLogger.error(ex4.getMessage(), ex4);
					response.sendError(HttpServletResponse.SC_NOT_FOUND,
							ex4.getMessage());
				}

			}

		} else if (command.equals(TheApp.CMD_ZU_MECS_AUSWEISE_ZUTRITT)) {
			PersonalzutrittsklasseDto[] dtos = getZutrittscontrollerFac()
					.personalzutrittsklassenFindByTGueltigab(
							new Timestamp(System.currentTimeMillis()),
							theclientDto);

			ArrayList<StringBuffer> alDaten = new ArrayList<StringBuffer>();

			for (int i = 0; i < dtos.length; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append("10");

				// Hole personalDto
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKeySmall(dtos[i].getPersonalIId());

				sb.append(Helper.fitString2Length(personalDto.getCAusweis(),
						20, ' '));
				sb.append(Helper.fitString2Length(personalDto.getCPersonalnr()
						.toString(), 10, ' '));
				sb.append(Helper.fitString2Length("", 24, ' '));

				// Hole Zutrittsklasse
				ZutrittsklasseDto zutrittsklasseDto = getZutrittscontrollerFac()
						.zutrittsklasseFindByPrimaryKey(
								dtos[i].getZutrittsklasseIId());
				sb.append(Helper.fitString2Length(zutrittsklasseDto.getCNr(),
						3, ' '));
				alDaten.add(sb);
			}

			// Besucherausweise
			String[] ausweise = getZutrittscontrollerFac()
					.zutrittonlinecheckAusweiseFindByTGueltigab(
							new Timestamp(System.currentTimeMillis()));

			for (int i = 0; i < ausweise.length; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append("10");
				sb.append(Helper.fitString2Length(ausweise[i], 20, ' '));
				sb.append(Helper.fitString2Length("", 10, ' '));
				sb.append(Helper.fitString2Length("", 24, ' '));
				sb.append(Helper.fitString2Length(
						ZutrittscontrollerFac.ZUTRITTSKLASSE_ONLINECHECK, 3,
						' '));
				alDaten.add(sb);
			}

			// sortieren
			String datenGesamt = "";
			for (int i = alDaten.size() - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					if ((new String(alDaten.get(j))).compareTo(new String(
							alDaten.get(j + 1))) > 0) {
						StringBuffer lagerbewegungDtoTemp = alDaten.get(j);
						alDaten.set(j, alDaten.get(j + 1));
						alDaten.set(j + 1, lagerbewegungDtoTemp);
					}
				}
			}

			for (int i = 0; i < alDaten.size(); i++) {

				StringBuffer sbTemp = alDaten.get(i);
				sbTemp.append("\r\n");
				datenGesamt += new String(sbTemp);
			}

			myLogger.info(command + ":" + datenGesamt);
			getTheClient(request, response).setSMsg(datenGesamt);
		} else if (command.startsWith(TheApp.CMD_ZU_MECS_ZUTRITT_ONLINE_CHECK)) {
			String termid = request.getParameter("termid");

			String card = request.getParameter("card");
			String pin = request.getParameter("pin");

			try {
				ZutrittsobjektDto dto = getZutrittscontrollerFac()
						.zutrittsobjektFindByCNr(termid);
				boolean b = getZutrittscontrollerFac()
						.onlineCheck(card, pin,
								new Timestamp(System.currentTimeMillis()),
								dto.getIId());
				if (b == true) {
					myLogger.info(command + ": ZUTRITT ERLAUBT");
					getTheClient(request, response).setSMsg("A");

				} else {
					myLogger.info(command + ": ZUTRITT VERWEIGERT");
					getTheClient(request, response).setSMsg("Z");
				}
			} catch (EJBExceptionLP ex4) {
				if (ex4.getCause() instanceof EJBExceptionLP) {
					EJBExceptionLP lpex = (EJBExceptionLP) ex4.getCause();
					if (lpex.getCode() == EJBExceptionLP.FEHLER_BEI_FIND) {

						response.sendError(HttpServletResponse.SC_NOT_FOUND,
								"Zutrittsobjekt '" + termid
										+ "' nicht angelegt");
						myLogger.error("Zutrittsobjekt '" + termid
								+ "' nicht angelegt", ex4);
					} else {
						myLogger.error(ex4.getMessage(), ex4);
						response.sendError(HttpServletResponse.SC_NOT_FOUND,
								ex4.getMessage());
					}
				}
			}

		} else if (command.startsWith(TheApp.CMD_ZU_MECS_ZUTRITT_EVENTS)) {
			String termid = request.getParameter("termid");

			if (termid == null || termid.length() == 0) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Es ist der Parameter termid='LanPortName' erforderlich");
				return null;
			}

			ZutrittsobjektDto dto = null;
			try {
				dto = getZutrittscontrollerFac()
						.zutrittsobjektFindByCNr(termid);
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_BEI_FIND) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND,
							"Zutrittsobjekt '" + termid + "' nicht angelegt");
					return null;
				} else {
					e.printStackTrace();
					response.sendError(
							HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Unerwarteter Fehler aufgetreten.");
					return null;
				}
			}

			String s = getZutrittscontrollerFac().getZutrittsEventsFuerMecs(
					dto.getIId(), theclientDto);
			myLogger.info(command + ":" + s);
			getTheClient(request, response).setSMsg(s);

		} else if (command.startsWith(TheApp.CMD_ZU_MECS_MAXTRANSNR)) {
			getTheClient(request, response).setSMsg("999");
		} else if (command.startsWith(TheApp.CMD_ZU_MECS_LOG)) {

			String record = command.substring(17);
			ZutrittslogDto dto = new ZutrittslogDto();

			Calendar c = Calendar.getInstance();
			String zeitpunkt = record.substring(5, 19);
			int iJahr = new Integer(zeitpunkt.substring(0, 4));
			int iMonat = new Integer(zeitpunkt.substring(4, 6)) - 1;
			int iTag = new Integer(zeitpunkt.substring(6, 8));
			int iStunden = new Integer(zeitpunkt.substring(8, 10));
			int iMinuten = new Integer(zeitpunkt.substring(10, 12));
			int iSekunden = new Integer(zeitpunkt.substring(12, 14));

			c.set(iJahr, iMonat, iTag, iStunden, iMinuten, iSekunden);
			dto.setTZeitpunkt(new Timestamp(c.getTimeInMillis()));
			String personalnr = record.substring(19, 24);
			String erlaubt = record.substring(24, 27);
			String objekt = record.substring(46, 67).trim();
			String ausweis = record.substring(106, 135).trim();
			String event = record.substring(126, 137).trim();

			PersonalDto personalDto = getPersonalFac().personalFindByCAusweis(
					ausweis);

			if (personalDto != null || event.equals("PINONLINE")
					|| personalnr.equals("?????") || personalnr.equals("     ")) {
				if (personalDto != null) {
					dto.setCPerson(getPartnerFac().partnerFindByPrimaryKey(
							personalDto.getPartnerIId(), theclientDto)
							.formatFixAnredeTitelName2Name1());
					dto.setMandantCNr(personalDto.getMandantCNr());
				} else if (personalnr.equals("     ")) {
					dto.setCPerson("Besucher");
					dto.setMandantCNr(mandant);
				} else if (event != null && event.equals("PINONLINE")) {
					dto.setCPerson("Tempor\u00E4rer Pin-Code");
					dto.setMandantCNr(mandant);
				} else {
					dto.setCPerson("Unbekannt");
					dto.setMandantCNr(mandant);
				}

				if (erlaubt.equals("ZZ1")) {
					dto.setBErlaubt(Helper.boolean2Short(true));
				} else {
					dto.setBErlaubt(Helper.boolean2Short(false));
				}

				dto.setCAusweis(ausweis);
				dto.setCZutrittscontroller(null);

				try {
					ZutrittsobjektDto zutrittsobjektDto = getZutrittscontrollerFac()
							.zutrittsobjektFindByCNr(objekt);

					dto.setCZutrittsobjekt(zutrittsobjektDto.getBezeichnung()
							+ "-" + zutrittsobjektDto.getCAdresse());
					dto.setCZutrittscontroller(getZutrittscontrollerFac()
							.zutrittscontrollerFindByPrimaryKey(
									zutrittsobjektDto
											.getZutrittscontrollerIId())
							.getCNr());
					dto.setMandantCNrObjekt(zutrittsobjektDto.getMandantCNr());
				} catch (RemoteException ex6) {
					dto.setCZutrittsobjekt("Zutrittsobjekt unbekannt");
				}

				getZutrittscontrollerFac().createZutrittslog(dto);
			}

			myLogger.info(command);

		} else if (command.startsWith(TheApp.CMD_ZU_MECS_TEMPLATES)) {

			String sAendern = request.getParameter("changedsince");

			PersonalfingerDto[] personalfingerDtos = null;
			if (sAendern == null) {
				personalfingerDtos = getZutrittscontrollerFac()
						.personalfingerFindAll();

			} else {

				Calendar c = Calendar.getInstance();
				int iJahr = new Integer(sAendern.substring(0, 4));
				int iMonat = new Integer(sAendern.substring(4, 6)) - 1;
				int iTag = new Integer(sAendern.substring(6, 8));
				int iStunden = new Integer(sAendern.substring(8, 10));
				int iMinuten = new Integer(sAendern.substring(10, 12));

				c.set(iJahr, iMonat, iTag, iStunden, iMinuten, 0);
				c.set(Calendar.MILLISECOND, 0);

				personalfingerDtos = getZutrittscontrollerFac()
						.personalfingerFindByTAendern(
								new java.sql.Timestamp(c.getTimeInMillis()),
								theclientDto);
			}
			StringBuffer sb = new StringBuffer();
			// Zuerts alle loeschen
			sb.append(Helper.fitString2LengthAlignRight("0", 5, ' '));
			sb.append(Helper.fitString2LengthAlignRight("0", 2, ' '));
			sb.append(Helper.fitString2Length("X", 512, 'X'));
			StringBuffer zeit = new StringBuffer();
			Calendar cAendern = Calendar.getInstance();
			zeit.append(Helper.fitString2Length(cAendern.get(Calendar.YEAR)
					+ "", 4, '0'));
			zeit.append(Helper.fitString2Length(
					(cAendern.get(Calendar.MONTH) + 1) + "", 2, '0'));
			zeit.append(Helper.fitString2Length(
					cAendern.get(Calendar.DAY_OF_MONTH) + "", 2, '0'));
			zeit.append(Helper.fitString2Length(
					cAendern.get(Calendar.HOUR_OF_DAY) + "", 2, '0'));
			zeit.append(Helper.fitString2Length(cAendern.get(Calendar.MINUTE)
					+ "", 2, '0'));

			sb.append(zeit);
			sb.append("\r\n");

			for (int i = 0; i < personalfingerDtos.length; i++) {
				PersonalfingerDto personalfingerDto = personalfingerDtos[i];
				sb.append(Helper.fitString2LengthAlignRight(
						personalfingerDto.getIId() + "", 5, ' '));
				sb.append(Helper.fitString2LengthAlignRight("1", 2, ' '));
				String templateBase64 = new String(
						org.apache.commons.codec.binary.Base64
								.encodeBase64(personalfingerDto.getOTemplate1()));
				sb.append(Helper.fitString2Length(templateBase64, 512, ' '));

				cAendern = Calendar.getInstance();
				cAendern.setTimeInMillis(personalfingerDto.getTAendern()
						.getTime());
				zeit = new StringBuffer();
				zeit.append(Helper.fitString2Length(cAendern.get(Calendar.YEAR)
						+ "", 4, '0'));
				zeit.append(Helper.fitString2Length(
						(cAendern.get(Calendar.MONTH) + 1) + "", 2, '0'));
				zeit.append(Helper.fitString2Length(
						cAendern.get(Calendar.DAY_OF_MONTH) + "", 2, '0'));
				zeit.append(Helper.fitString2Length(
						cAendern.get(Calendar.HOUR_OF_DAY) + "", 2, '0'));
				zeit.append(Helper.fitString2Length(
						cAendern.get(Calendar.MINUTE) + "", 2, '0'));

				sb.append(zeit);
				sb.append("\r\n");

				if (personalfingerDto.getOTemplate2() != null) {
					sb.append(Helper.fitString2LengthAlignRight(
							personalfingerDto.getIId() + "", 5, ' '));
					sb.append(Helper.fitString2LengthAlignRight("2", 2, ' '));
					templateBase64 = new String(
							org.apache.commons.codec.binary.Base64
									.encodeBase64(personalfingerDto
											.getOTemplate2()));
					sb.append(Helper.fitString2Length(templateBase64, 512, ' '));
					sb.append(zeit);
					if (i == personalfingerDtos.length - 1) {
						// sb.append("\r");
					} else {
						sb.append("\r\n");
					}
				}

			}
			getTheClient(request, response).setSMsg(new String(sb));

		}

		return getSJSPNext();
	}

	private String getMeldungGebuchtFuerBDE(Object data, String taetigkeit,
			TheClientDto theClientDto) throws Exception {

		HashMap<?, ?> hmParameter = (HashMap<?, ?>) data;
		ZeitdatenDto zeitdatenDto = (ZeitdatenDto) hmParameter.get("zeitdaten");
		String person = (String) hmParameter.get("person");
		String beleg = (String) hmParameter.get("beleg");
		MaschineDto maschine = (MaschineDto) hmParameter.get("maschine");

		Integer taetigkeitIId_Kommt = getZeiterfassungsFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT,
						theClientDto).getIId();
		Integer taetigkeitIId_Geht = getZeiterfassungsFac()
				.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT,
						theClientDto).getIId();

		String tmp = "Letzte Buchung: " + person + ", ";
		if (beleg != null) {
			tmp += beleg + ", ";
		}

		if (hmParameter.containsKey("fertig")) {
			tmp += " Fertig-Buchung, ";
		} else {
			if (maschine != null
					&& maschine.getBezeichnung().trim().length() > 0) {
				tmp += " Maschine: " + maschine.getBezeichnung() + ", ";
			}
		}

		if (zeitdatenDto.getTaetigkeitIId() != null) {
			tmp += "Sondert\u00E4tigkeit: " + taetigkeit;
		} else {
			if (taetigkeit != null) {
				tmp += "T\u00E4tigkeit: " + taetigkeit;
			}
		}

		tmp += " um "
				+ Helper.formatTimestamp(zeitdatenDto.getTZeit(), new Locale(
						"de", "AT"));

		// Meldung ob KOMMT/GEHT fehlt
		Timestamp tBis = new Timestamp(zeitdatenDto.getTZeit().getTime() + 100);
		ZeitdatenDto[] zeitdatenDtos = getZeiterfassungsFac()
				.zeitdatenFindZeitdatenEinesTagesUndEinerPersonOnheBelegzeiten(
						zeitdatenDto.getPersonalIId(),
						Helper.cutTimestamp(zeitdatenDto.getTZeit()), tBis);
		boolean bKommtfuerHeuteFehlt = true;
		for (int i = 0; i < zeitdatenDtos.length; i++) {
			ZeitdatenDto zeitdatenDtoZeile = zeitdatenDtos[i];
			if (zeitdatenDtoZeile.getTaetigkeitIId() != null
					&& zeitdatenDtoZeile.getTaetigkeitIId().equals(
							taetigkeitIId_Kommt)) {
				bKommtfuerHeuteFehlt = false;

				break;
			}
		}

		if (bKommtfuerHeuteFehlt == true) {
			tmp += "<br><font color=\"#FF0000\">KOMMT f\u00FCr heute fehlt</font>";
		}

		Timestamp tLetztesGeht = null;
		Timestamp tLetztesKommt = null;

		// Meldung ob GEHT ind den etzten Tagen fehlt
		// Hole letztes GEHT
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria liste = session
				.createCriteria(FLRZeitdaten.class);
		liste.add(Expression.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
				zeitdatenDto.getPersonalIId()));
		liste.add(Expression.eq(ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
				taetigkeitIId_Geht));
		liste.add(Expression.lt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				Helper.cutTimestamp(zeitdatenDto.getTZeit())));

		liste.addOrder(Order.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
		liste.setMaxResults(1);
		List<?> letzerAuftrag = liste.list();

		Iterator<?> it = letzerAuftrag.iterator();

		if (it.hasNext()) {
			FLRZeitdaten flrLetzerAuftrag = (FLRZeitdaten) it.next();
			tLetztesGeht = new Timestamp(flrLetzerAuftrag.getT_zeit().getTime());
		}
		session.close();

		session = FLRSessionFactory.getFactory().openSession();
		liste = session.createCriteria(FLRZeitdaten.class);
		liste.add(Expression.eq(ZeiterfassungFac.FLR_ZEITDATEN_PERSONAL_I_ID,
				zeitdatenDto.getPersonalIId()));
		liste.add(Expression.eq(ZeiterfassungFac.FLR_ZEITDATEN_TAETIGKEIT_I_ID,
				taetigkeitIId_Kommt));
		liste.add(Expression.lt(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT,
				Helper.cutTimestamp(zeitdatenDto.getTZeit())));

		liste.addOrder(Order.desc(ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT));
		liste.setMaxResults(1);
		letzerAuftrag = liste.list();

		it = letzerAuftrag.iterator();

		if (it.hasNext()) {
			FLRZeitdaten flrLetzerAuftrag = (FLRZeitdaten) it.next();
			tLetztesKommt = new Timestamp(flrLetzerAuftrag.getT_zeit()
					.getTime());
		}

		if (tLetztesGeht != null && tLetztesKommt != null) {
			if (tLetztesGeht.before(tLetztesKommt)) {
				tmp += "<br><font color=\"#FF0000\">Letzte GEHT-Buchung fehlt.</font>";
			}
		}
		if (tLetztesKommt != null && tLetztesGeht == null) {
			tmp += "<br><font color=\"#FF0000\">Letzte GEHT-Buchung fehlt.</font>";
		}

		return tmp;

	}

}
