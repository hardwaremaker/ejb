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
package com.lp.server.system.automatikjob;

import com.lp.server.bestellung.service.BSMahnlaufDto;
import com.lp.server.system.service.AutoMahnenDto;
import com.lp.server.system.service.AutoMahnungsversandDto;
import com.lp.server.system.service.TheClientDto;

public class AutomatikjobMahnen extends AutomatikjobBasis {

	private AutoMahnungsversandDto automahnungsversandDto;
	private AutoMahnenDto autoMahnenDto;
	private boolean errorInJob;

	public AutomatikjobMahnen() {
		super();
	}

	/**
	 * performJob
	 * 
	 * @todo Diese com.lp.server.system.automatikjob.AutomatikjobBasis-Methode
	 *       implementieren
	 */
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("start Mahnlauferstellung");
		errorInJob = false;

		try {
			automahnungsversandDto = getAutoMahnungsversandFac()
					.autoMahnungsversandFindByMandantCNr(theClientDto.getMandant());
			autoMahnenDto = getAutoMahnenFac().autoMahnenFindByMandantCNr(theClientDto.getMandant());
		} catch (Throwable t) {
			myLogger.error("Fehler beim holen der Parameter");
			errorInJob = true;
		}
		BSMahnlaufDto mahnlaufDto = null;
		boolean bNotToPerform = false;
		try {
			bNotToPerform = getBSMahnwesenFac().bGibtEsEinenOffenenBSMahnlauf(
					theClientDto.getMandant(), theClientDto);
		} catch (Throwable t) {
			myLogger.error("Fehler beim initialisieren");
			errorInJob = true;
		}
		if (bNotToPerform) {
			myLogger
					.info("Offene Mahnl\u00E4ufe vorhanden... Kein neuer Mahnlauf wird generiert");
			errorInJob = true;
		} else {
			try {
				if(autoMahnenDto.getBAbMahnen()){
					if(autoMahnenDto.getBLieferMahnen()){
						mahnlaufDto = getBSMahnwesenFac()
						.createABMahnungenUndLieferMahnungenUndLiefererinnerungen(theClientDto);
					} else {
						mahnlaufDto = getBSMahnwesenFac().createBSMahnlaufABMahnungen(theClientDto);
					}
				} else {
					if(autoMahnenDto.getBLieferMahnen()){
						mahnlaufDto = getBSMahnwesenFac().createBSMahnlaufEchteLiefermahnungen(theClientDto);
					}
				}
			
			} catch (Throwable t) {
				myLogger.error("Fehler beim erstellen des Mahnlaufes " + t.getMessage());
				errorInJob = true;
			}
		}
		if (mahnlaufDto != null) {
			int iMahniID = mahnlaufDto.getIId();
			automahnungsversandDto.setMahnlaufIId(iMahniID);
		} else {
			automahnungsversandDto.setMahnlaufIId(null);
		}
		try {
			getAutoMahnungsversandFac().updateAutoMahnungsversand(
					automahnungsversandDto);
		} catch (Throwable t) {
			myLogger.error("Fehler beim zur\u00FCckschreiben der Parameter");
			errorInJob = true;
		}
		myLogger.info("ende Mahnlaufgenerierung");
		return errorInJob;
	}

}
