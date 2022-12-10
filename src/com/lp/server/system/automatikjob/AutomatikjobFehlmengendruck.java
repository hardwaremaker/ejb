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

import com.lp.server.system.ejbfac.HvPrinter;
import com.lp.server.system.service.AutoFehlmengendruckDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class AutomatikjobFehlmengendruck extends AutomatikjobBasis {

	public AutomatikjobFehlmengendruck() {
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
			myLogger.info("start Fehlmengendruck");
			AutoFehlmengendruckDto autoFehlmengendruckDto = getAutoFehlmengendruckFac()
					.autoFehlmengendruckFindByMandantCNr(theClientDto.getMandant());
			JasperPrintLP print = getFertigungReportFac()
					.printAufloesbareFehlmengen(Helper.SORTIERUNG_NACH_IDENT,
							false, false, theClientDto);
			
			String usedPrinter = autoFehlmengendruckDto.getCDrucker();
			HvPrinter hvPrinter = getServerDruckerFacLocal().createHvPrinter(usedPrinter);
			if (getServerDruckerFacLocal().exists(hvPrinter))
				getServerDruckerFacLocal().print(print, hvPrinter);
			
		} catch (Throwable ex) {
			myLogger.error("Fehler beim drucken der Fehlmengen", ex);
			return true;
		}
		myLogger.info("ende Fehlmengendruck");
		return false;
	}

}
