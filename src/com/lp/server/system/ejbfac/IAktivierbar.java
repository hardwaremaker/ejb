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
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import com.lp.server.rechnung.ejbfac.RechnungPruefbarFacBean;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.BelegDto;
import com.lp.service.BelegpositionDto;
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
 * Wird von den FacBeans implementiert (zB.: {@link RechnungPruefbarFacBean})
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
	BelegPruefungDto aktiviereBeleg(Integer iid, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	/**
	 * Wird vom {@link BelegAktivierungController} verwendet.
	 * Berechnet den Wert des Beleges.
	 * Es werden keine Pr&uuml;fungen durchgef&uuml;hrt.
	 * @param iid
	 * 	die IId des Beleges (zb Auftrag, Rechnung...)
	 * @param theClientDto
	 * @return den Zeitpunkt tAendern des Beleges, 
	 * wenn dieser im Zuge der Berechnung gesetzt wird 
	 * (muss frisch aus der Datenbank kommen!).
	 * Sonst den aktuellen Zeitpunkt.
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	Timestamp berechneBeleg(Integer iid, TheClientDto theClientDto) 
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
	 * Pr&uuml;ft ob alle "rechtlichen" Bedingungen erf&uuml;llt sind, 
	 * um den Status des Belegs seinen Status von Angelegt auf Offen
	 * (bzw das in das jeweilige belegabh&auml;ngige Pendant) zu &auml;ndern.
	 * Wirft eine EJBExceptionLP mit einem entsprechendem Fehlercode wenn nicht.</br>
	 * <p>Beispielsweise, ob der Anwender das Recht hat, einen Beleg zu aktivieren</p> 
	 * @param belegId
	 * 	die IId des Beleges (zb Auftrag, Rechnung...)
	 * @param theClientDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	void pruefeAktivierbarRecht(Integer belegId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	/**
	 * Wird vom {@link BelegAktivierungController} verwendet.<br>
	 * Gibt alle Timestamps des Beleges in eine Liste zur&uuml;ck,
	 * welche einen &Auml;nderungszeitpunkt darstellen. Dazu geh&ouml;ren
	 * zB. auch Storierungszeitpunkt und tManuellErledigt.
	 * @param iid
	 * 	die IId des Beleges (zb Auftrag, Rechnung...)
	 * @return eine Liste mit allen &Auml;nderungszeitpunkten. Darf nicht null und auch nicht leer sein.
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	List<Timestamp> getAenderungsZeitpunkte(Integer iid)
			throws EJBExceptionLP, RemoteException;
	
	BelegDto getBelegDto(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;
	
	BelegpositionDto[] getBelegPositionDtos(Integer iid, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;
	
	Integer getKundeIdDesBelegs(BelegDto belegDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException ;
}
