package com.lp.server.system.service;

import java.io.Serializable;
import java.rmi.RemoteException;
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
import java.util.List;

import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.system.ejbfac.IntelligenterStklImportFacBean;
import com.lp.service.BelegpositionDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;

/**
 * Dieses Interface stellt Methoden zur Verf&uuml;gung, welche die
 * {@link IntelligenterStklImportFacBean} ben&ouml;tigt, um die gefundenen
 * Results eines St&uuml;cklistenimports zu importieren und die
 * nach dem jeweiligen St&uuml;cklistenimports entsprechenden Positionen
 * in der DB anlegt.
 * 
 * @author andi
 *
 */
public interface IImportPositionen extends Serializable{

	/**
	 * Erstellt ein leeres spezifisches PositionDto.
	 * 
	 * @return neues PositionDto
	 */
	public BelegpositionDto getNewPositionDto();
	
	/**
	 * F&uuml;llt ein PositionDto aus dem ImportResult des Intelligenten
	 * St&uuml;cklistenimports mit den entsprechenden spezifischen Variablen.
	 * 
	 * @param posDto zu bef&uuml;llendes PositionDto
	 * @param spez Spezifikation des St&uuml;cklistenimports
	 * @param result einzelnes ImportResultobjekt
	 * @param theClientDto der aktuelle Benutzer
	 * 
	 * @return das spezifisch aufgef&uuml;llte PositionDto
	 */
	public BelegpositionDto preparePositionDtoAusImportResult(
			BelegpositionDto posDto, StklImportSpezifikation spez, 
			IStklImportResult result, TheClientDto theClientDto);
	
	/**
	 * Erstellt aus den Dtos die Positionen in der DB
	 * 
	 * @param posDtos die zu persistierenden Daten
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	public void createPositions(List<BelegpositionDto> posDtos, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;
	
}
