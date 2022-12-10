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
import java.util.Iterator;
import java.util.Map;

import com.lp.server.system.ejb.ParametermandantPK;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;

public class AutomatikjobInternebestellungberechnen extends AutomatikjobBasis {

	private boolean errorInJob;

	public AutomatikjobInternebestellungberechnen() {
		super();
	}

	/**
	 * performJob
	 * 
	 * @todo Diese com.lp.server.system.automatikjob.AutomatikjobBasis-Methode
	 *       implementieren
	 */
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("start Internen Bestellung");
		errorInJob = false;

		Date today = new Date(Calendar.getInstance().getTimeInMillis());
		ParametermandantPK parametermandantPK = new ParametermandantPK(
				ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_INTERNEBESTELLUNG,
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG);
		ParametermandantDto parametermandantDto = null;
		try {
			parametermandantDto = getParameterFac()
					.parametermandantFindByPrimaryKey(parametermandantPK);
		} catch (Throwable t) {
			myLogger.error("Fehler beim suchen der Parameter");
			errorInJob = true;
		}
		Integer mandantParameterTageAuftrag = Integer.parseInt(parametermandantDto
				.getCWert());
		parametermandantPK = new ParametermandantPK(
				ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_INTERNEBESTELLUNG_UNTERLOSE,
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG);
		try {
			parametermandantDto = getParameterFac()
					.parametermandantFindByPrimaryKey(parametermandantPK);
		} catch (Throwable t) {
			myLogger.error("Fehler beim suchen der Parameter");
			errorInJob = true;
		}
		Integer mandantParameterTageUnterlos = Integer.parseInt(parametermandantDto
				.getCWert());
		parametermandantPK = new ParametermandantPK(
				ParameterFac.TOLERANZFRIST_INTERNE_BESTELLUNG,
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG);
		try {
			parametermandantDto = getParameterFac()
					.parametermandantFindByPrimaryKey(parametermandantPK);
		} catch (Throwable t) {
			myLogger.error("Fehler beim suchen der Parameter");
			errorInJob = true;
		}
		Integer mandantParameterToleranz = Integer.parseInt(parametermandantDto
				.getCWert());
		parametermandantPK = new ParametermandantPK(
				ParameterFac.PARAMETER_INTERNEBESTELLUNG_VERDICHTUNGSZEITRAUM,
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG);
		try {
			parametermandantDto = getParameterFac()
					.parametermandantFindByPrimaryKey(parametermandantPK);
		} catch (Throwable t) {
			myLogger.error("Fehler beim suchen der Parameter");
			errorInJob = true;
		}

		Integer iVerdichtungsTage = Integer.parseInt(parametermandantDto
				.getCWert());

		parametermandantPK = new ParametermandantPK(
				ParameterFac.PARAMETER_LAGERMIN_JE_LAGER,
				theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL);
		try {
			parametermandantDto = getParameterFac()
					.parametermandantFindByPrimaryKey(parametermandantPK);
		} catch (Throwable t) {
			myLogger.error("Fehler beim suchen der Parameter");
			errorInJob = true;
		}

		boolean lagerMinJeLager = (Boolean) parametermandantDto
				.getCWertAsObject();

	
		try {

			

				getInternebestellungFac().erzeugeInterneBestellung(true,
						mandantParameterTageAuftrag, mandantParameterTageUnterlos,mandantParameterToleranz, today,
						false, iVerdichtungsTage, true, null,false,null, null,true,theClientDto);

			

		} catch (Throwable t) {
			myLogger.error("Fehler beim erstellen der Internen Bestellung");
			errorInJob = true;
		}

		myLogger.info("ende Internen Bestellung");
		return errorInJob;
	}
}
