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
package com.lp.webapp.frame;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

/**
 * <p>
 * frame; Von dieser Klasse leiten alle Servlets ab.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; 30.10.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2010/11/11 14:23:43 $
 */
abstract public class FrameServlet extends HttpServlet implements
		SingleThreadModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			this.getClass());

	private static String FS = "/";
	private static String sJSPDir = FS + "jsp" + FS;

	public FrameServlet() {
		// nothing here
	}

	/**
	 * Hole, generiere die Clientklasse, hier werden alle relevanten
	 * Client-daten gepeichert.
	 * 
	 * @param request
	 *            Description of Parameter
	 * @param response
	 *            Description of Parameter
	 * @return The client value
	 * @exception Exception
	 *                Description of Exception
	 */
	public TheClient getTheClient(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		TheClient client = null;
		HttpSession session = request.getSession(true);
		
		//WILDFLY SP7675
		if (request.getParameter("JSESSIONID") != null) {
		    Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
		    response.addCookie(userCookie);
		} else {
		    String sessionId = session.getId();
		    Cookie userCookie = new Cookie("JSESSIONID", sessionId);
		    response.addCookie(userCookie);
		}
		
		System.out.println("session geholt: " + session);
		System.out.println("CREATION_TIME:"+new java.sql.Timestamp( session.getCreationTime()));
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

	/**
	 * @param config
	 *            Description of Parameter
	 * @exception ServletException
	 *                Description of Exception
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			super.init(config);
		} catch (Exception e) {
			log("**E: init: " + e.getMessage());
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TheClient client = null;
		String jspNext = null;
		ICommand cmd = null;
		String commandToDo = null;
		try {
			// String commandToDo = HelperServlet.extractCommand(request);
			int i = 0;

			commandToDo = getCommand(request);

			// hole (oder lege einen neu an) von der Session den TheClient.
			client = (TheClient) getTheClient(request, response);
			// --session angelegt

			myLogger.logData(client.getIdSession() + ": " + commandToDo);

			// Aktionen vor dem Ausfuehren des Command.
			cmd = lookupCommand(commandToDo, client, request, response);

			response.setHeader("Cache-Control", "must-revalidate, no-store");
			Date now = new Date();
			response.setDateHeader("Date", now.getTime());
			jspNext = cmd.execute(request, response);
		} catch (Exception e) {
			// --Programmiererfehler; treten im Betrieb nicht auf ;-)
			jspNext = "error.jsp";

			if (commandToDo == null || !commandToDo.startsWith("MECS")) {

				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String stacktrace = sw.toString();

				client.setSMsg("Fataler Fehler" + "<br><br>" + stacktrace);
			} else {
				client.setSMsg("Fataler Fehler");
			}
			log2FindABug(client, request);
			myLogger.error("FrameServlet.doPost", e);
		}

		request.setAttribute("com.lp.client", client);
		try {
			if (!getTheClient(request, response).isBResponseIsReady()) {
				getServletConfig().getServletContext()
						.getRequestDispatcher(getSJSPDir() + jspNext)
						.forward(request, response);
			} else {
				//SP1602+PJ15890
				getTheClient(request, response).setBResponseIsReady(false);
			}
		} catch (Exception ex) {
			// --Programmiererfehler; treten im Betrieb nicht auf ;-)
			jspNext = "error.jsp";
			client.setSMsg("Fataler Fehler");
			log2FindABug(client, request);
			myLogger.error("FrameServlet.doPost", ex);
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
		// / if (command == null) {
		// command = TheApp.CMD_WAP_SHOWMENUE;
		// }
		return command;

	}

	/**
	 * Achtung: JO; 13.12.02; Httpdocu<BR>
	 * isNew public boolean isNew() Returns true if the client does not yet know
	 * about the session or if the client chooses not to join the session. For
	 * example, if the server used only cookie-based sessions, and the client
	 * had disabled the use of cookies, then a session would be new on each
	 * request.Returns:true if the server has created a session, but the client
	 * has not yet joinedThrows:java.lang.IllegalStateException - if this method
	 * is called on an already invalidated session
	 * 
	 * @param request
	 *            Description of Parameter
	 * @return The sessionNew value
	 */
	protected boolean isSessionNew(final HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		return (session.getAttribute(session.getId()) == null);
	}

	/**
	 * Description of the Method
	 * 
	 * @param request
	 *            Description of Parameter
	 * @param client
	 *            Description of Parameter
	 */
	protected void log2FindABug(TheClient client, HttpServletRequest request) {
		if (client != null && request != null) {
			myLogger.error("hdlbug >>>: " + "; sid: "
					+ request.getSession().getId() + "; getUserName: "
					+ client.getUserName() + "; getQueryString: "
					+ request.getQueryString() + "; getRemoteHost: "
					+ request.getRemoteHost() + "; getRemoteUser: "
					+ request.getRemoteUser() + "; getRemoteAddr: "
					+ request.getRemoteAddr() + "; cmd: "
					+ request.getParameter("cmd") + "getParameterForLog: "
					+ HelperServlet.getParameterForLog(request));
		}
	}

	/**
	 * Pruefe ob Session abgelaufen; stelle Command ein.
	 * 
	 * @param client
	 *            Description of Parameter
	 * @param sessionNew
	 *            Description of Parameter
	 * @param commandToDo
	 *            Description of Parameter
	 * @param request
	 *            Description of Parameter
	 * @return Description of the Returned Value
	 */
	protected synchronized ICommand sessionCheck(String commandToDo,
			boolean sessionNew, TheClient client, HttpServletRequest request) {

		ICommand cmd = null;
		// if (sessionNew) {
		// --entweder echte neue session oder abgelaufen
		// else {
		// if (commandToDo.equals(TheApp.CMD_LOGOUT)) {
		// //--session abgelaufen und logout nachher geklickt:
		// //wie logout mit session behandeln
		// client.setAuthenticatedMsg(null);
		// }
		// else {
		// client.setAuthenticatedMsg(client.getLocalStringForLabel(
		// "Hinweis.Session.Abgelaufen", Frame.FRAME_LANGBUNDLE));
		// }
		// cmd = lookupCommand(TheApp.CMD_LOGOUT, client);
		// client.setAuthenticationCode(Login.SESSION_TIMEOUT);
		// // client.setRequestDoesNotMatchCommand(true);
		// }
		// }
		// else {
		// //--Session innerhalb Sessiontimeout
		// if (commandToDo == null) {
		// cmd = lookupCommand(TheApp.CMD_LOGIN, client);
		// }
		// else {
		// try {
		// cmd = lookupCommand(commandToDo, client);
		// }
		// catch (UnsupportedOperationException ce) {
		// cmd = lookupCommand(TheApp.CMD_LOGOUT, client);
		// client.getInformant().setAndRespectInformation("Command >" +
		// commandToDo +
		// "< wird nicht unterstuetzt; " + ce.getMessage());
		// MsgLogger.instance().printlnHintLog("Command " + commandToDo +
		// "geht nicht :-(");
		// }
		// }
		// }
		//
		// //postcondition
		// if (cmd == null) {
		// throw new
		// NullPointerException("FrameServlet postCondition: cmd==null " +
		// commandToDo);
		// }

		return cmd;
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
	abstract protected ICommand lookupCommand(String commandToDo,
			TheClient client, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	public void setSJSPDir(String sJSPDirI) {
		FrameServlet.sJSPDir = sJSPDirI;
	}

	public String getSJSPDir() {
		return sJSPDir;
	}
}
