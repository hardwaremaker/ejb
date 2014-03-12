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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lp.webapp.frame.CommandError;
import com.lp.webapp.frame.FrameServlet;
import com.lp.webapp.frame.ICommand;
import com.lp.webapp.frame.TheApp;
import com.lp.webapp.frame.TheClient;

public class ZeiterfassungServlet extends FrameServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZeiterfassungServlet() {
	}

	protected synchronized ICommand lookupCommand(String commandToDo,
			TheClient client, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (commandToDo.equals(TheApp.CMD_ZE_BDESTATION)) {
			return new CommandZE("bdestation.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_BDESTATION2)) {
			return new CommandZE("bdestation2.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_BDESTATION3)) {
			return new CommandZE("bdestation3.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_BDESTATION3GUTSCHLECHT)) {
			return new CommandZE("bdestation3gutschlecht.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_BDESTATION4)) {
			return new CommandZE("bdestation4.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_QUICKZEITERFASSUNG)) {
			return new CommandZE("quickzeiterfassung.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_QUICKZE)) {
			return new CommandZE("quickze.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_RECHNERSTART1)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_MECS_PERSSTAMM)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_MECS_AUSWEISE)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_MECS_ONLCHECK)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_MECS_ONLINECHECK_ABL)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_MECS_SALDO)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ANWESENHEITSLITE)) {
			return new CommandAnwesenheitsliste(null);
		} else if (commandToDo.equals(TheApp.CMD_ZE_MECS_ZEITBUCHEN)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_MECS_ZEITBUCHENFINGERPRINT)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_PARAM)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_AUSWEISE_ZUTRITT)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_RELAIS)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZE_MECS_ERLAUBTETAETIGKEITEN)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_ZUTRITT)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_TEMPLATES)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_TERMINAL)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_ZUTRITT_ONLINE_CHECK)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_ZUTRITT_EVENTS)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.equals(TheApp.CMD_ZU_MECS_MAXTRANSNR)) {
			return new CommandZE("mecs.jsp");
		} else if (commandToDo.startsWith(TheApp.CMD_ZU_MECS_LOG)) {
			return new CommandZE("mecs.jsp");
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			getTheClient(request, response).setSMsg(
					"**E: what to do? commandToDo: " + commandToDo);
			return new CommandError("error.jsp");
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

}
