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
package com.lp.server.system.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.ejbfac.BelegAktivierungController;
import com.lp.util.EJBExceptionLP;
/**
 * Wird von den Beleg Facades (zb. {@link RechnungFac}) geerbt 
 * und vom BelegAktivierungController implementiert.
 * Die Implementierungen der Methoden in den FacBeans m&uuml;ssen
 * eine Instanz des BelegAktivierungController erzeugen
 * und auf dessen gleichnamige Implementierung delegieren.
 * @author robert
 */
public interface IAktivierbarControlled {
	
	/**
	 * <b>Darf erst nach <code>berechneBelegControlled()</code> aufgerufen werden!</b><br>
	 * Weist den {@link BelegAktivierungController} an,
	 * den Status des Beleges von Angelegt auf Offen
	 * (bzw das in das jeweilige belegabh&auml;ngige Pendant)
	 * zu &auml;ndern.<br>
	 * Der Controller pr&uuml;ft, ob alle Voraussetzungen
	 * getroffen sind und ob nach dem Zeitpunkt <code>t</code> &Auml;nderungen
	 * im jeweiligen Beleg gemacht wurden. 
	 * @param iid die IId des Beleges (zb Auftrag, Rechnung...)
	 * @param theClientDto
	 * @param t der Zeitpunkt, zu dem der anfragende Client
	 * 	die Methode <code>berechneBelegControlled()</code> zuletzt aufgerufen hat. }
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	void aktiviereBelegControlled(Integer iid, Timestamp t, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	/**
	 * Weist den {@link BelegAktivierungController} an,
	 * den Wert des Beleges zu berechnen.
	 * Der Controller pr&uuml;ft, ob alle Voraussetzungen
	 * getroffen sind.
	 * @param iid
	 * 	die IId des Beleges (zb Auftrag, Rechnung...)
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * @return den Zeitpunkt, zu welchem die Berechnungen gemacht wurden.
	 * Dieser Wert muss an die Methode <code>aktiviereBelegControlled()</code>
	 * &uuml;bergeben werden.
	 */
	Timestamp berechneBelegControlled(Integer iid, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;
}
