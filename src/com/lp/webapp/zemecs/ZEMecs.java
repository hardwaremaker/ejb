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
package com.lp.webapp.zemecs;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.GleitzeitsaldoDto;
import com.lp.server.personal.service.MonatsabrechnungDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.Helper;
import com.lp.webapp.frame.TheClient;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
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
 * @version not attributable Date $Date: 2012/05/02 07:58:31 $
 */

public class ZEMecs extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String sUser = "lpwebappzemecs";
	/**
	 * @deprecated MB. klasse sollte eigentlich von FrameServlet erben, und dort
	 *             steht ein logger zur verfuegung
	 */
	private static ILPLogger myLogger = null;
	private PersonalFac personalFac = null;
	private PartnerFac partnerFac = null;
	private LogonFac logonFac = null;
	private TheClientDto theClientDto = null;
	private ZeiterfassungFac zeiterfassungFac = null;

	private static String FS = "/";

	private static String sJSPDir = FS + "jsp" + FS;

	// Initialize global variables
	public void init() throws ServletException {

		myLogger = LPLogService.getInstance().getLogger(this.getClass());

		try {
			Context context = new InitialContext();
			personalFac = (PersonalFac) context.lookup("PersonalFac");
			zeiterfassungFac = (ZeiterfassungFac) context
					.lookup("ZeiterfassungFac");
			partnerFac = (PartnerFac) context.lookup("PartnerFac");
			logonFac = (LogonFac) context.lookup("LogonFac");

			/*theClientDto = logonFac.logon(
					Helper.getFullUsername(sUser),
					Helper.getMD5Hash((sUser + "lpwebappzemecs").toCharArray()),
					new Locale("de", "AT"), null, 
					new Timestamp(System.currentTimeMillis()));*/
			
			theClientDto = logonFac.logonIntern(new Locale("de","AT"), null);
			
		} catch (Exception e) {
			myLogger.error("Fehler beim holen der Fac", e);
		}

	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		myLogger.logKritisch("doPost - Client:'" + request.getRemoteAddr()
				+ "' Req:'" + request.getQueryString() + "'");

		String file = request.getParameter("file");
		String cmd = request.getParameter("cmd");
		String record = request.getParameter("record");
		if (file != null) {

			if (file.equals("MECS_PERSSTAMM")) {
				// Personalstamm holen

				StringBuffer sb = new StringBuffer();

				// unbedingt nach personalnummer sortieren
				PersonalDto[] personalDtos = personalFac
						.personalFindByCAusweisSortiertNachPersonalnr();
				for (int i = 0; i < personalDtos.length; i++) {
					PersonalDto personalDto = personalDtos[i];
					personalDto.setPartnerDto(partnerFac
							.partnerFindByPrimaryKey(personalDto
									.getPartnerIId(), theClientDto));
					StringBuffer tmp = new StringBuffer();

					tmp.setLength(0);
					tmp.append(fitString2LengthAlignRight(personalDto
							.getCPersonalnr()
							+ "", 5, '0')); // persnr
					tmp.append(fitString2Length("", 3, ' ')); // zutrkl

					String sVorname = personalDto.getPartnerDto()
							.getCName2vornamefirmazeile2();
					String sNachname = personalDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();

					if (sVorname == null) {
						sVorname = "";
					}

					tmp.append(fitString2Length(sVorname + " " + sNachname, 25,
							' ')); // name
					sb.append(tmp).append("\r\n");

				}
				PrintWriter p = response.getWriter();
				p.println(new String(sb));
				p.flush();

			} else if (file.equals("MECS_AUSWEISE")) {
				// Ausweisnummern holen
				StringBuffer sb = new StringBuffer();
				PersonalDto[] personalDtos = personalFac
						.personalFindByCAusweisSortiertNachCAusweis();
				for (int i = 0; i < personalDtos.length; i++) {
					PersonalDto personalDto = personalDtos[i];
					personalDto.setPartnerDto(partnerFac
							.partnerFindByPrimaryKey(personalDto
									.getPartnerIId(), theClientDto));
					StringBuffer tmp = new StringBuffer();
					// unbedingt nach ausweis sortieren
					tmp.setLength(0);
					tmp.append(fitString2Length(personalDto.getCAusweis(), 20,
							' ')); // ausweis
					tmp.append(fitString2LengthAlignRight(personalDto
							.getCPersonalnr()
							+ "", 5, '0')); // persnr
					tmp.append(fitString2Length("", 3, ' ')); // zutrkl

					String sVorname = personalDto.getPartnerDto()
							.getCName2vornamefirmazeile2();
					String sNachname = personalDto.getPartnerDto()
							.getCName1nachnamefirmazeile1();

					if (sVorname == null) {
						sVorname = "";
					}

					tmp.append(fitString2Length(sVorname + " " + sNachname, 25,
							' ')); // name
					sb.append(tmp).append("\r\n");

				}
				PrintWriter p = response.getWriter();
				p.println(new String(sb));
				p.flush();

			} else if (file.equals("MECS_SALDO")) {
				String ausweis = "";
				try {
					ausweis = request.getParameter("ausweis");
				} catch (Exception e) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Parameter 'ausweis' nicht angegeben");
					myLogger.error("doPost; Exception aufgetreten", e);
					return;
				}
				ausweis = ausweis.trim();
				PersonalDto personalDto = personalFac
						.personalFindByCAusweis(ausweis);
				personalDto.setPartnerDto(partnerFac.partnerFindByPrimaryKey(
						personalDto.getPartnerIId(), theClientDto));

				java.sql.Timestamp ts = new java.sql.Timestamp(System
						.currentTimeMillis() - 3600000 * 24);
				ts = com.lp.util.Helper.cutTimestamp(ts);

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(ts.getTime());

				MonatsabrechnungDto report = zeiterfassungFac
						.erstelleMonatsAbrechnung(personalDto.getIId(),
								new Integer(c.get(Calendar.YEAR)), new Integer(
										c.get(Calendar.MONTH)), false,
								new java.sql.Date(ts.getTime()), theClientDto,false,ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER, null);

				GleitzeitsaldoDto gleitzeitsaldoDto = personalFac
						.gleitzeitsaldoFindByPersonalIIdIJahrIMonat(personalDto
								.getIId(), new Integer(c.get(Calendar.YEAR)),
								new Integer(c.get(Calendar.MONTH)));
				BigDecimal saldo = gleitzeitsaldoDto.getNSaldomehrstunden(); // .
				// add
				// (
				// gleitzeitsaldoDto
				// .
				// getNSaldouestdpauschale());
				saldo = saldo.add(gleitzeitsaldoDto.getNSaldouestfrei100());
				saldo = saldo.add(gleitzeitsaldoDto.getNSaldouestfrei50());
				saldo = saldo
						.add(gleitzeitsaldoDto.getNSaldouestpflichtig100());
				saldo = saldo.add(gleitzeitsaldoDto.getNSaldouestpflichtig50());

				StringBuffer sb = new StringBuffer();

				sb.append(fitString2Length("Zeitsaldo:", 20, ' ')); // text
				sb.append(fitString2Length(saldo.toString(), 20, ' ')); // saldo
				sb.append("\r\n");

				BigDecimal urlaub = report.getNVerfuegbarerurlaub();
				if (urlaub == null) {
					urlaub = new BigDecimal(0);
				}

				sb.append(fitString2Length("Urlaub:", 20, ' ')); // text
				sb.append(fitString2Length(urlaub.toString(), 20, ' ')); // saldo
				sb.append("\r\n");

				PrintWriter p = response.getWriter();
				p.println(sb);
				p.flush();
			}
		}
		// Schreibt Zeitdaten in DB
		else if (record != null) {
			String schluesselNr = record.substring(19, 39).trim();
			String zeit = record.substring(5, 19);
			String taetigkeit = record.substring(3, 5);

			ZeitdatenDto zeitdatenDto = new ZeitdatenDto();

			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, new Integer(zeit.substring(0, 4)).intValue());
			c.set(Calendar.MONTH,
					new Integer(zeit.substring(4, 6)).intValue() - 1);
			c.set(Calendar.DAY_OF_MONTH, new Integer(zeit.substring(6, 8))
					.intValue());
			c.set(Calendar.HOUR_OF_DAY, new Integer(zeit.substring(8, 10))
					.intValue());
			c.set(Calendar.MINUTE, new Integer(zeit.substring(10, 12))
					.intValue());
			c.set(Calendar.SECOND, new Integer(zeit.substring(12, 14))
					.intValue());

			zeitdatenDto
					.setTZeit(new java.sql.Timestamp(c.getTime().getTime()));
			// Taetigkeiten, die MECS liefert koenen in Konfigurations- XML
			// eingestellt werden,
			// daher werden sie hier Hard-Codiert:
			if (taetigkeit.equals("B1")) {
				zeitdatenDto.setTaetigkeitIId(zeiterfassungFac
						.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_KOMMT,
								theClientDto).getIId());
			} else if (taetigkeit.equals("B2")) {
				zeitdatenDto.setTaetigkeitIId(zeiterfassungFac
						.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_GEHT,
								theClientDto).getIId());
			} else if (taetigkeit.equals("B3")) {
				zeitdatenDto.setTaetigkeitIId(zeiterfassungFac
						.taetigkeitFindByCNr(ZeiterfassungFac.TAETIGKEIT_UNTER,
								theClientDto).getIId());
			} else if (taetigkeit.equals("B5")) {
				zeitdatenDto.setTaetigkeitIId(zeiterfassungFac
						.taetigkeitFindByCNr(
								ZeiterfassungFac.TAETIGKEIT_BEHOERDE, theClientDto)
						.getIId());
			}
			zeitdatenDto.setCWowurdegebucht("MECS-Terminal");
			zeitdatenDto.setTAendern(zeitdatenDto.getTZeit());

			// Wenn hier NullPointerException, dann kann kein Personal mit
			// Ausweisnummer gefunden werden
			zeitdatenDto.setPersonalIId(personalFac.personalFindByCAusweis(
					schluesselNr).getIId());

			zeiterfassungFac.createZeitdaten(zeitdatenDto, true, true,false, false, theClientDto);

			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();

		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Kein zul\u00E4ssiger Parameter angegeben!");
		}
	}

	public void setSJSPDir(String sJSPDirI) {
		ZEMecs.sJSPDir = sJSPDirI;
	}

	public String getSJSPDir() {
		return sJSPDir;
	}

	public TheClient getTheClient(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		TheClient client = null;
		HttpSession session = request.getSession(true);
		System.out.println("session geholt: " + session);

		try {
			client = (TheClient) session.getAttribute(session.getId());
			System.out.println("client geholt: " + client);
		} catch (Exception e) {
			client = null;
		}
		if (client == null) {
			client = new TheClient();
			session.setAttribute(session.getId(), client);

			client.setIdSession(session.getId());

			client.setSContextPath(request.getContextPath());
			client.setSServletPath(request.getServletPath());
			client.setSURLClient(new java.net.URL("http", request
					.getServerName(), request.getServerPort(), request
					.getRequestURI()));
			System.out.println("client angelegt: " + client);
		}
		return client;
	}

	private String fitString2LengthAlignRight(String string2Fit, int newLength,
			char fillUpChar) {
		if (string2Fit == null || string2Fit.length() == newLength) {
			return string2Fit;
		}

		String retVal = null;
		if (string2Fit.length() > newLength) {
			retVal = string2Fit.substring(0, newLength);
		} else {
			StringBuffer sb = new StringBuffer(string2Fit);
			while (sb.length() < newLength) {
				sb.insert(0, fillUpChar);
			}
			retVal = sb.toString();
		}
		return retVal;
	}

	private String fitString2Length(String string2Fit, int newLength,
			char fillUpChar) {
		if (string2Fit == null || string2Fit.length() == newLength) {
			return string2Fit;
		}

		String retVal = null;
		if (string2Fit.length() > newLength) {
			retVal = string2Fit.substring(0, newLength);
		} else {
			StringBuffer sb = new StringBuffer(string2Fit);
			while (sb.length() < newLength) {
				sb.append(fillUpChar);
			}

			retVal = sb.toString();
		}
		return retVal;
	}

	// Clean up resources
	public void destroy() {
		try {
			logonFac.logout(theClientDto);
		} catch (java.rmi.RemoteException ex) {
			// logout Fehlgeschlagen
		}
	}
}
