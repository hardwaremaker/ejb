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
package com.lp.server.util;

import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;


public abstract class LPScriptEngine extends ScriptEngine {

	/**
	 * Der Modul-Name
	 * @return den Modul-Namen
	 */
	protected abstract String getModule() ;

	/**
	 * Der Name des Scripts das aufgerufen werden soll
	 *
	 * @return den aufzurufenden Script-Namen
	 */
	protected abstract String getScriptName() ;

	private SystemFac systemFac ;
	private TheClientDto theClientDto ;
	
	private boolean optionalScript ;
	private boolean processed ;
	
	public LPScriptEngine(SystemFac systemFac, TheClientDto theClientDto) {
		this.systemFac = systemFac ;
		this.theClientDto = theClientDto ;
	}

	public <T> T runEmbeddedLPScript(String scriptName) throws Throwable {
		setProcessed(false) ;
		T value = null ;
		String fileContent = getFileContent(scriptName) ;
		if(fileContent != null) {
			value = (T) getEngine().runScriptlet(fileContent) ;
			setProcessed(true) ;
		}

		return value ;
	}

	public <T> T runEmbeddedLPScript() throws Throwable {
		return runEmbeddedLPScript(getScriptName()) ;
	}

	private String getFileContent(String filename) throws Throwable {
		return isOptionalScript() 
				? systemFac.getOptionalScriptContentFromLPDir(getModule(), filename, theClientDto.getMandant(), theClientDto.getLocUi(), null)
				: systemFac.getScriptContentFromLPDir(getModule(), filename, theClientDto.getMandant(), theClientDto.getLocUi(), null) ;
	}

	/**
	 * Handelt es sich um ein optionales Script?
	 * Ein optionales Script wirft wenn es nicht gefunden wird keine EJBException
	 * 
	 * @return true wenn es sich um ein optionales Script handelt
	 */
	public boolean isOptionalScript() {
		return optionalScript;
	}

	public void beOptionalScript() {
		setOptionalScript(true);
	}
	
	public void beMandatoryScript() {
		setOptionalScript(false);
	}
	
	public void setOptionalScript(boolean optionalScript) {
		this.optionalScript = optionalScript;
	}

	/**
	 * Konnte das Script erfolgreich aufgerufen werden</br>
	 * <p>Ist in F&auml;llen interessant, wenn im Script mehrere 
	 * Methoden aufgerufen werden</p> 
	 * @return true wenn das Script erfolgreich aufgerufen werden konnte. 
	 * False wenn zum Beispiel kein Script gefunden worden ist
	 */
	public boolean isProcessed() {
		return processed;
	}

	protected void setProcessed(boolean processed) {
		this.processed = processed;
	}
}
