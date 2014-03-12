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

import java.sql.Timestamp;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.Helper;
import com.lp.webapp.frame.Command;
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
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2010/04/01 14:00:45 $
 */
public class CommandZE extends Command {

	private static final String sUser = "lpwebappzemecs";

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
		String command = null;
		command = request.getParameter("cmd");
		if (command == null) {
			command = TheApp.CMD_WAP_SHOWMENUE;
		}
		return command;

	}

	public String execute(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		super.execute(req, resp);

		if (command.equals(TheApp.CMD_WAP_ZEIT_BUCHEN)) {
			// -- buchen
			ZeitdatenDto zeitdatenDto = new ZeitdatenDto();
			TheClient tc = getTheClient(req, resp);
			DataZE dZE = (DataZE) tc.getData();
			zeitdatenDto.setTZeit(new Timestamp(dZE.getBuchungsZeitpunkt()
					.getTime()));
			
			TheClientDto theclientDto = getLogonFac()
					.logon( Helper.getFullUsername(sUser), 			
							Helper.getMD5Hash((sUser + new String("lpwebappzemecs")).toCharArray()),
							getMandantFac().getLocaleDesHauptmandanten(), null, null,
							new Timestamp(System.currentTimeMillis()));


			String sBuArt = req.getParameter(TheApp.BUCHUNGSART);

			String sTaeArt = ZeiterfassungFac.TAETIGKEIT_KOMMT;
			if (sBuArt.equals(TheApp.GEHEN)) {
				sTaeArt = ZeiterfassungFac.TAETIGKEIT_GEHT;
			} else if (sBuArt.equals(TheApp.UNTERBRECHEN)) {
				sTaeArt = ZeiterfassungFac.TAETIGKEIT_UNTER;
			}

			zeitdatenDto.setTaetigkeitIId(getZeiterfassungsFac()
					.taetigkeitFindByCNr(sTaeArt, theclientDto).getIId());

			zeitdatenDto.setPersonalIId(getPersonalFac()
					.personalFindByCAusweis(dZE.getSAusweisnr()).getIId());

			getZeiterfassungsFac().createZeitdaten(zeitdatenDto, true, true,false,
					theclientDto);
		}

		return getSJSPNext();
	}

}
