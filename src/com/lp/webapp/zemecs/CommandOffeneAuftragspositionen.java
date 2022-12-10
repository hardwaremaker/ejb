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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;
import com.lp.webapp.frame.Command;
import com.lp.webapp.frame.TheApp;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.HtmlExporter;

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
 * @version not attributable Date $Date: 2011/06/07 05:02:41 $
 */
public class CommandOffeneAuftragspositionen extends Command {

	private static final String sUser = "lpwebappzemecs";

	public CommandOffeneAuftragspositionen(String sJSPI) {
		super(sJSPI);
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.execute(request, response);

		Locale localeLogon = getMandantFac().getLocaleDesHauptmandanten();

		String locale = request.getParameter("locale");

		if (locale != null && locale.length() > 3) {
			localeLogon = new Locale(locale.substring(0, 2), locale.substring(2, 4));
		}

		if (command.equals(TheApp.CMD_OFFENE_AUFRAGSPOSITIONEN)) {
			String mandant = request.getParameter("mandant");

			/*
			 * TheClientDto theclientDto = getLogonFac()
			 * .logon(Helper.getFullUsername(sUser), Helper.getMD5Hash((sUser +
			 * "lpwebappzemecs") .toCharArray()), localeLogon, mandant, new
			 * Timestamp(System.currentTimeMillis()));
			 */

			TheClientDto theclientDto = getLogonFac().logonIntern(localeLogon, mandant);
			if (mandant == null) {
				mandant = theclientDto.getMandant();
			}

			ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER;

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(mandant,
					ParameterFac.KATEGORIE_AUFTRAG,
					ParameterFac.PARAMETER_OFFENE_AUFTRAGSPOSITIONEN_WERKTAGE_IN_ZUKUNFT);

			Integer iWerktage = (Integer) parameterDto.getCWertAsObject();

			Calendar cStichtag = Calendar.getInstance();
			cStichtag.setTimeInMillis(System.currentTimeMillis());
			int iAzahlWerkTageVersetzt = 0;
			while (iWerktage > iAzahlWerkTageVersetzt) {
				boolean bArbeitstag = true;

				cStichtag.add(Calendar.DAY_OF_MONTH, 1);

				if (cStichtag.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
						|| cStichtag.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					bArbeitstag = false;
				}

				if (bArbeitstag == true) {
					iAzahlWerkTageVersetzt++;
				}

			}

			java.sql.Date dStichtag = Helper.cutDate(new java.sql.Date(cStichtag.getTimeInMillis()));

			Integer fertigungsgruppeIId = null;

			String fertigungsgruppe = request.getParameter("fertigungsgruppe");
			if (fertigungsgruppe != null && fertigungsgruppe.length() > 0) {
				FertigungsgruppeDto ftgDto = getStuecklisteFac()
						.fertigungsgruppeFindByMandantCNrCBezOhneExc(theclientDto.getMandant(), fertigungsgruppe);

				if (ftgDto != null) {
					fertigungsgruppeIId = ftgDto.getIId();
				} else {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Fertigungsgruppe '" + fertigungsgruppe + "' konnte nicht gefunden werden");
				}
			}

			JasperPrintLP jasperprint = getAuftragReportFac().printAuftragOffeneDetails(krit, false, dStichtag, true,
					false, null, null, null, null, null, 0,
					AuftragReportFac.REPORT_AUFTRAG_OFFENE_ARTUNVERBINDLICH_ALLE, false, false, fertigungsgruppeIId,
					theclientDto);

			if (jasperprint != null) {
				try {
					response.setContentType("text/html");
					response.setLocale(localeLogon);
					// PrintWriter out = response.getWriter();
					StringBuffer out = new StringBuffer();
					JRExporter exporter = new HtmlExporter();
					Map<Object, Object> imagesMap = new HashMap<Object, Object>();
					request.getSession().setAttribute("IMAGES_MAP", imagesMap);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperprint.getPrint());
					exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, out);

					
					exporter.exportReport();
					out = HelperServer.removeScriptHtml(out);

					// Add refresh

					String str2 = out.toString().replaceAll("</title>",
							"</title><meta http-equiv=\"refresh\" content=\"300\">");
					// to get a StringBuffer result:
					out = new StringBuffer(str2);

					response.getWriter().print(out);

					getTheClient(request, response).setBResponseIsReady(true);
				} catch (JRException ex) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
				}
			}
			getLogonFac().logout(theclientDto);

		}

		return getSJSPNext();
	}

}
