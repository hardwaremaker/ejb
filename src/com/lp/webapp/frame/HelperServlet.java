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
package com.lp.webapp.frame;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

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
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/07 12:59:40 $
 */
public class HelperServlet {
	public final static String CRLF = "\r\n";

	private HelperServlet() {
		// nothing here
	}

	/**
	 * Gets the parameterForLog attribute of the MsgLogger object
	 * 
	 *@param request
	 *            Description of Parameter
	 *@return The parameterForLog value
	 */
	static public String getParameterForLog(HttpServletRequest request) {
		return getParameterAs(CRLF, request);
	}

	/**
	 * Gets the parameterAs attribute of the MsgLogger object
	 * 
	 *@param cRLF
	 *            Description of Parameter
	 *@param request
	 *            Description of Parameter
	 *@return The parameterAs value
	 */
	static private String getParameterAs(String cRLF, HttpServletRequest request) {
		String param = "No parameters!";
		Enumeration<?> params = request.getParameterNames();
		if (params.hasMoreElements()) {
			int num = 0;
			param = "";
			while (params.hasMoreElements()) {
				String p = (String) params.nextElement();
				param += "name (" + num + ") : " + p;
				String[] values = request.getParameterValues(p);
				int l = 0;
				while (l < values.length) {
					param += "; value (" + l + ") : " + values[l] + cRLF;
					l++;
				}
				num++;
			}
		}
		return param;
	}

	/**
	 * Description of the Method
	 * 
	 *@param request
	 *            Description of Parameter
	 *@return Description of the Returned Value
	 */
	// static public String extractCommand(HttpServletRequest request) {
	// String command = "no command";
	// command = request.getParameter("cmd");
	// if (command == null) {
	// command = TheApp.CMD_WAP_SHOWMENUE;
	// }
	// return command;
	// }
}
