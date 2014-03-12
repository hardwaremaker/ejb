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
package com.lp.webapp.heliumv;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;
import com.lp.webapp.frame.CommandError;
import com.lp.webapp.frame.FrameServlet;
import com.lp.webapp.frame.ICommand;
import com.lp.webapp.frame.TheApp;
import com.lp.webapp.frame.TheClient;

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
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/05/26 09:08:29 $
 */

public class HeliumVServlet extends FrameServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String sUser = "lpwebappzemecs";
	private ZeiterfassungFac zeiterfassungFac = null;
	private LogonFac logonFac = null;
	private String idUser = null;
	private PersonalFac personalFac = null;

	// Initialize global variables
	public void init() throws ServletException {
		try {
			Context context = new InitialContext();
			zeiterfassungFac = (ZeiterfassungFac) context
					.lookup("lpserver/ZeiterfassungFacBean/remote");
			logonFac = (LogonFac) context.lookup("lpserver/LogonFacBean/remote");
			TheClientDto theClientDto = logonFac.logon( 
					Helper.getFullUsername(sUser),				
					Helper.getMD5Hash((sUser + "lpwebappzemecs").toCharArray()),
					new Locale("de", "AT"), null, null, new java.sql.Timestamp(
							System.currentTimeMillis()));

			personalFac = (PersonalFac) context.lookup("lpserver/PersonalFacBean/remote");
		} catch (Exception e) {
			myLogger.logKritisch("Fehler beim holen der Fac", e);
		}
	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// command holen
		String cmd = request.getParameter("cmd");
		String sJspNext = null;
		String auswertung = request.getParameter("auswertung");

		if (auswertung != null) {
			handleSubmit(request, response);
		} else {
			super.doGet(request, response);
		}
	}

	/**
	 * getCommand
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	protected String getCommand(HttpServletRequest request) {
		String command = null;
		command = request.getParameter("cmd");
		if (command == null) {
			command = TheApp.CMD_WAP_SHOWMENUE;
		}
		return command;

	}

	protected void handleSubmit(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Get report file name from params
		String auswertung = request.getParameter("auswertung");

		// get the name filter
		String mandant = request.getParameter("mandant");

		String sUser= "lpwebappzemecs";
		TheClientDto theClientDto = logonFac.logon(
				Helper.getFullUsername(sUser),		
				Helper.getMD5Hash((sUser + new String("lpwebappzemecs")).toCharArray()), 
				new Locale("de", "AT"), mandant, null, 
				new java.sql.Timestamp(System.currentTimeMillis()));

		JasperPrintLP jasperprint = null;
		if (auswertung == null) {
			// Keine Auswertung angegeben
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Parameter 'auswertung' nicht angegeben");
			return;
		}

		if (auswertung.equals("anwesenheitsliste")) {
			jasperprint = zeiterfassungFac.printAnwesenheitsliste(theClientDto);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Kein zul\u00E4ssiger Parameter angegeben!");
			return;
		}

		if (jasperprint != null) {
			try {
				response.setContentType("text/html");
				//PrintWriter out = response.getWriter();
				StringBuffer out = new StringBuffer();
				JRExporter exporter = new JRHtmlExporter();
				Map<Object, Object> imagesMap = new HashMap<Object, Object>();
				request.getSession().setAttribute("IMAGES_MAP", imagesMap);
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperprint.getPrint());
				exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, out);

				exporter.setParameter(
						JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
						Boolean.FALSE);

				exporter.exportReport();
				out = HelperServer.removeScriptHtml(out);
				response.getWriter().print(out);
			} catch (JRException ex) {
				response.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex
								.getMessage());
			}
		}
	}

	// Clean up resources
	public void destroy() {
	}

	/**
	 * Description of the Method
	 * 
	 * @param commandToDo
	 *            Description of Parameter
	 * @param client
	 *            Description of Parameter
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return Description of the Returned Value
	 * @throws Exception
	 */
	protected synchronized ICommand lookupCommand(String commandToDo,
			TheClient client, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (commandToDo.equals(TheApp.CMD_WAP_SHOWMENUE)) {
			return new CommandZE("wap_menue.jsp");
		} else if (commandToDo.equals(TheApp.CMD_WAP_SHOWLOGIN)) {
			return new CommandLogin("wap_login.jsp");
		} else if (commandToDo.equals(TheApp.CMD_WAP_DOLOGIN)) {
			return new CommandLogin("wap_ze.jsp");
		} else if (commandToDo.equals(TheApp.CMD_WAP_ZEIT_BUCHEN)) {
			return new CommandZE("wap_ze_gebucht.jsp");
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			getTheClient(request, response).setSMsg(
					"**E: what to do? commandToDo: " + commandToDo);
			return new CommandError("error.jsp");
		}
	}

}
