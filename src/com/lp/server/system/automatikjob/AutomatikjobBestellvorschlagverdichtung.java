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

import com.lp.server.system.ejb.ParametermandantPK;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;

public class AutomatikjobBestellvorschlagverdichtung extends AutomatikjobBasis {

	private boolean errorInJob;

	public AutomatikjobBestellvorschlagverdichtung() {
		super();
	}

	/**
	 * performJob
	 * 
	 * @todo Diese com.lp.server.system.automatikjob.AutomatikjobBasis-Methode
	 *       implementieren
	 */
	public boolean performJob(TheClientDto theClientDto) {
		try {
			myLogger.info("start Bestellvorschlagverdichtung");
			errorInJob = false;
			boolean bIsLocked = false;
			try {
				getBestellvorschlagFac()
						.pruefeBearbeitenDesBestellvorschlagsErlaubt(theClientDto);
			} catch (Throwable t) {
				myLogger
						.info("Bestellvorschlag ist von einem anderen Benutzer gesperrt. Job wird nicht durchgef\u00FChrt");
				bIsLocked = true;
				errorInJob = true;
			}
			if (!bIsLocked) {
				ParametermandantPK parametermandantPK = new ParametermandantPK(
						ParameterFac.BESTELLVORSCHLAG_VERDICHTUNGSZEITRAUM_TAGE,
						theClientDto.getMandant(), ParameterFac.KATEGORIE_BESTELLUNG);
				ParametermandantDto parametermandantDto = null;
				try {
					parametermandantDto = getParameterFac()
							.parametermandantFindByPrimaryKey(
									parametermandantPK);
				} catch (Throwable t) {
					myLogger.error("Fehler beim suchen der Parameter");
					errorInJob = true;
				}
				
				
				boolean bProjektklammer=getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER, theClientDto.getMandant());
				
				getBestellvorschlagFac().verdichteBestellvorschlag(
						Long.parseLong(parametermandantDto.getCWert()), true,bProjektklammer,true,
						theClientDto);
			}
			try {
				getBestellvorschlagFac()
						.removeLockDesBestellvorschlagesWennIchIhnSperre(
								theClientDto);
			} catch (Throwable t) {
				myLogger
						.error("Fehler beim entfernen der Sperre... Bitte heben Sie die Sperre manuell \u00FCber einen Client auf.");
			}

		} catch (Throwable ex) {
			myLogger.error("Fehler beim erstellen des Bestellvorschlags");
			errorInJob = true;
		}
		myLogger.info("ende Bestellvorschlagverdichtung");
		return errorInJob;
	}

}
