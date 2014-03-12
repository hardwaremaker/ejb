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
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import com.lp.server.rechnung.ejbfac.RechnungFacBean;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

/**
 * Dieses Interface stellt die Methoden bereit, welche der
 * {@link BelegAktivierungController} ben&ouml;tigt, um:
 * <li>eine korrekte
 * &Uuml;berpr&uuml;fung eines Beleges auf dessen Berechenbarkeit (f&uuml;r
 * den Druck) und Aktivierbarkeit zu gew&auml;hrleisten</li>
 * <li>die Status&auml;nderung von Angelegt auf Offen (bzw. von/auf
 * die Belegabh&auml;ngigen Pendants) durchzuf&uuml;hren</li>
 * <li>die Werte des Beleges zu berechnen.</li>
 * <li>auf (fremde) &Auml;nderungen nach der letzten Berechnung zu pr&uuml;fen.</li><br><br>
 * Wird von den FacBeans implementiert (zB.: {@link RechnungFacBean})
 * @author robert
 *
 */
public interface IAktivierbar {
	
	/**
	 * Wird vom {@link BelegAktivierungController} verwendet.
	 * &Auml;ndert den Status des Beleges von Angelegt auf Offen
	 * (bzw das in das jeweilige belegabh&auml;ngige Pendant).
	 * Es werden keine Pr&uuml;fungen durchgef&uuml;hrt.
	 * @param iid die IId des Beleges (zb Auftrag, Rechnung...)
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	void aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	/**
	 * Wird vom {@link BelegAktivierungController} verwendet.
	 * Berechnet den Wert des Beleges.
	 * Es werden keine Pr&uuml;fungen durchgef&uuml;hrt.
	 * @param iid
	 * 	die IId des Beleges (zb Auftrag, Rechnung...)
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	void berechneBeleg(Integer iid, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;
	
	/**
	 * Wird vom {@link BelegAktivierungController} verwendet.
	 * Pr&uuml;ft ob alle Bedingungen erf&uuml;llt sind, um die Werte
	 * des Beleges zu berechnen oder seinen Status von Angelegt auf Offen
	 * (bzw das in das jeweilige belegabh&auml;ngige Pendant) zu &auml;ndern.
	 * Wirft eine EJBExceptionLP mit einem entsprechendem Fehlercode wenn nicht.
	 * @param iid
	 * 	die IId des Beleges (zb Auftrag, Rechnung...)
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	void pruefeAktivierbar(Integer iid, TheClientDto theClientDto) 
			throws EJBExceptionLP, RemoteException;
	
	/**
	 * Wird vom {@link BelegAktivierungController} verwendet.
	 * Wurde der Beleg in den Kopfdaten, eine seiner Positionen oder eine
	 * andere Abh&auml;ngigkeit <b>nach</b> dem Zeitpunkt <code>t</code> ge&auml;ndert?
	 * @param iid
	 * 	die IId des Beleges (zb Auftrag, Rechnung...)
	 * @param t der Zeitpunkt
	 * @return true wenn es eine sp&auml;tere &Auml;nderung gibt
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	boolean hatAenderungenNach(Integer iid, Timestamp t)
			throws EJBExceptionLP, RemoteException;
}
