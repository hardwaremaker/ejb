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
package com.lp.server.system.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;

@Remote
public interface IntelligenterStklImportFac {
	
	/**
	 * Maximale Anzahl der Treffer der Artikelsuche.
	 */
	public static final int MAX_ARTIKEL_FUER_AUSWAHL = 100;
	
	/**
	 * Sucht nach Artikeln f&uuml;r den Intelligenten Stklimport. 
	 * Leerzeilen werden entfernt.
	 * 
	 * @param spez Spezifikation der zu importierenden Datei
	 * @param importLines Zeilen der Importdatei als Rohdaten 
	 * @param rowIndex Nummer der Zeile in der Datei welche 
	 * <code>importLines.get(0)</code> entspricht.
	 * @param theClientDto der aktuelle Benutzer
	 * 
	 * @return Liste der ImportResults mit den gefundenen Artikeln
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public List<IStklImportResult> searchForImportMatches(StklImportSpezifikation spez,
			List<String> importLines, int rowIndex, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	/**
	 * Importiert die neuen Belegpositionen. Es werden dabei die gemeinsamen
	 * Variablen der PositionDtos gesetzt und je nach Spezifikation, die weiteren 
	 * in den jeweiligen Facs.
	 * Je nach Typ der Spezifikation werden die Positionen in die entsprechende 
	 * Datenbanktabelle eingef&uuml;gt.
	 * 
	 * @param spez Spezifikation der ImportResult
	 * @param results Liste der zu importierenden ImportResults
	 * @param updateArtikel true, wenn der Artikelstamm aktualisiert werden soll
	 * @param theClientDto der aktuelle Benutzer
	 * 
	 * @return Anzahl der importierten Positionen
	 */
	public int importiereImportResultsAlsBelegpositionen(StklImportSpezifikation spez,
			List<IStklImportResult> results, Boolean updateArtikel, 
			TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;
	
	/**
	 * Liefert alle verf&uuml;gbaren Spezifikationen f&uuml;r den aktuellen
	 * St&uuml;cklistentyp und den aktuellen Mandanten.
	 * 
	 * @param stklTyp Typ der St&uuml;ckliste als Konstante
	 * @param theClientDto der aktuelle Benutzer
	 * @return Map der gefundenen StklImportSpezifikationen
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public Map<String, StklImportSpezifikation> stklImportSpezifikationenFindAll(
			int stklTyp, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
	/**
	 * Speichert eine Spezifikation des Intelligenten Stklimports in der
	 * Datenbank ab.
	 * 
	 * @param spez die zu speichernde Spezifikation
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public void createStklImportSpezifikation(StklImportSpezifikation spez)
			throws RemoteException, EJBExceptionLP;
	
	/**
	 * L&ouml;scht eine Spezifikation des Intelligenten Stklimports aus
	 * der Datenbank.
	 * 
	 * @param spez die zu l&ouml;schende Spezifikation
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public void removeStklImportSpezifikation(StklImportSpezifikation spez)
			throws RemoteException, EJBExceptionLP;
	
	/**
	 * Aktualisiert eine Spezifikation des Intelligenten Stklimports in
	 * der Datenbank.
	 * 
	 * @param spez die zu aktualisierende Spezifikation
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	public void updateStklImportSpezifikation(StklImportSpezifikation spez)
			throws RemoteException, EJBExceptionLP;
	
}
