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
package com.lp.server.system.automatikjob;

import java.sql.Date;
import java.util.Calendar;

import com.lp.server.system.ejb.ParametermandantPK;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;

public class AutomatikjobBestellvorschlagberechnung extends AutomatikjobBasis {

	private boolean errorInJob;

	public AutomatikjobBestellvorschlagberechnung() {
		super();
	}

	/**
	 * performJob
	 * 
	 * @todo Diese com.lp.server.system.automatikjob.AutomatikjobBasis-Methode
	 *       implementieren
	 */
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("start Bestellvorschlagberechnung");
		errorInJob = false;

		Date today = new Date(Calendar.getInstance().getTimeInMillis());
		ParametermandantPK parametermandantPK = new ParametermandantPK(
				ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_AUFTRAG,
				theClientDto.getMandant(), ParameterFac.KATEGORIE_BESTELLUNG);
		ParametermandantDto parametermandantDto = null;
		try {
			parametermandantDto = getParameterFac()
					.parametermandantFindByPrimaryKey(parametermandantPK);
		} catch (Throwable t) {
			myLogger.error("Fehler beim suchen der Parameter");
			errorInJob = true;
		}
		Integer mandantParameterTage = Integer.parseInt(parametermandantDto
				.getCWert());
		parametermandantPK = new ParametermandantPK(
				ParameterFac.TOLERANZFRIST_BESTELLVORSCHLAG,
				theClientDto.getMandant(), ParameterFac.KATEGORIE_BESTELLUNG);
		try {
			parametermandantDto = getParameterFac()
					.parametermandantFindByPrimaryKey(parametermandantPK);
		} catch (Throwable t) {
			myLogger.error("Fehler beim suchen der Parameter");
			errorInJob = true;
		}
		Integer mandantParameterToleranz = Integer.parseInt(parametermandantDto
				.getCWert());

		// Checken ob gesperrt
		boolean bIsLocked = false;
		try {
			getBestellvorschlagFac()
					.pruefeBearbeitenDesBestellvorschlagsErlaubt(theClientDto);
		} catch (Throwable t) {
			myLogger.info("Bestellvorschlag ist von einem anderen Benutzer gesperrt. Job wird nicht durchgef\u00FChrt");
			bIsLocked = true;
			errorInJob = true;
		}
		try {
			if (!bIsLocked) {
				getBestellvorschlagFac().erstelleBestellvorschlag(
						mandantParameterTage, mandantParameterToleranz, today,
						null, null, false, false,true, theClientDto);
			}
		} catch (Throwable t) {
			myLogger.error("Fehler beim erstellen des Bestellvorschlags");
			errorInJob = true;
		}
		try {
			getBestellvorschlagFac()
					.removeLockDesBestellvorschlagesWennIchIhnSperre(
							theClientDto);
		} catch (Throwable t) {
			myLogger.error("Fehler beim entfernen der Sperre... Bitte heben Sie die Sperre manuell \u00FCber einen Client auf.");
		}
		myLogger.info("ende Bestellvorschlagberechnung");
		return errorInJob;
	}
}
