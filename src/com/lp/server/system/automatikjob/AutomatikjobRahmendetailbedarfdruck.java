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

import java.rmi.RemoteException;

import com.lp.server.system.ejbfac.HvPrinter;
import com.lp.server.system.service.AutoRahmendetailbedarfdruckDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

public class AutomatikjobRahmendetailbedarfdruck extends AutomatikjobBasis {

	public AutomatikjobRahmendetailbedarfdruck(){
		super();
	}
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("start Rahmendetailbedarfdruck");
		AutoRahmendetailbedarfdruckDto autoRahmendetailbedarfdruckDto = null;
		try {
			autoRahmendetailbedarfdruckDto = getAutoRahmendetailbedarfdruckFac().autoAutoRahmendetailbedarfdruckFindByMandantCNr(theClientDto.getMandant());
			if (autoRahmendetailbedarfdruckDto == null) return false;
		} catch (RemoteException e) {
			myLogger.error("Einstellungen f\u00FCr Job Rahmendetailbedarfdruck konnten nicht geladen werden", e);
			return true;
		}

		try {
			JasperPrintLP print = getRahmenbedarfeFac().printAlleOffenenRahmenbedarfe(autoRahmendetailbedarfdruckDto.getBSortiertnachArtikel(), theClientDto);
			HvPrinter hvPrinter = getServerDruckerFacLocal().createHvPrinter(autoRahmendetailbedarfdruckDto.getCDrucker());
			if (getServerDruckerFacLocal().exists(hvPrinter))
				getServerDruckerFacLocal().print(print, hvPrinter);
		} catch (Exception e) {
			myLogger.error("Der Druck Rahmendetailbedarf konnte nicht erstellt oder gedruckt werden:", e);
			return true;
		}
		
		myLogger.info("ende Rahmendetailbedarfdruck");
		return false;
	}

}
