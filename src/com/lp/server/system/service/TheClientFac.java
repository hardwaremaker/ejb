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
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Remote;

import com.lp.util.EJBExceptionLP;

@Remote
public interface TheClientFac {

	public static final String FLRSPALTE_C_BENUTZERNAME = "c_benutzername";
	public static final String FLRSPALTE_C_UILOCALE = "c_uisprache";
	public static final String FLRSPALTE_C_KONZERNLOCALE = "c_konzernsprache";
	public static final String FLRSPALTE_T_LOGGEDIN = "t_loggedin";
	public static final String FLRSPALTE_T_LOGGEDOUT = "t_loggedout";
	public static final String FLRSPALTE_I_PERSONAL = "i_personal";
	public static final String FLRSPALTE_SYSTEMROLLE_C_BEZ = "flrsystemrolle.c_bez";

	public static final int PK_INSTALLER = 1;

	public void createTheClient(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeTheClientTVonTBis(Timestamp tVon, Timestamp tBis)
			throws EJBExceptionLP, RemoteException;

//	public void removeTheClient(String cNr) throws EJBExceptionLP,
//			RemoteException;
//
//	public void removeTheClient(TheClientDto theClientDto)
//			throws EJBExceptionLP, RemoteException;

	public void updateTheClient(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateTheClients(TheClientDto[] theClientDtos)
			throws EJBExceptionLP, RemoteException;

	public TheClientDto theClientFindByPrimaryKey(String cNrUserI)
			throws EJBExceptionLP, RemoteException;
	
	public TheClientDto theClientFindByPrimaryKeyOhneExc(String cNrUserI);

	public TheClientDto theClientFindByUserLoggedIn(String cNr)
			throws EJBExceptionLP, RemoteException;

	public TheClientDto theClientFindByCBenutzernameLoggedIn(String cBenutzername)
	  		throws RemoteException;

	public void putInstaller(InstallerDto installerDto) throws RemoteException;

	public void removeInstaller() throws RemoteException;

	public boolean istNeuerClientVerfuegbar(Integer iBuildnummerclient)
			throws RemoteException;

	public InstallerDto getInstaller() throws RemoteException;

	public InstallerDto getInstallerWithoutClientFile() throws RemoteException;
	
	public InstallerDto getInstallerPart(Integer iPart) throws EJBExceptionLP;

	/**
	 * Alle noch angemeldeten Benutzer explizit ausloggen</br>
	 * <p>Es werden alle Theclient abgemeldet, die ein TLoggedOut == null haben</p>
	 * @param remove l&ouml;scht die abzumeldenden Benutzer auch gleich, ansonsten
	 *    wird als Abmeldezeitpunkt die aktuelle Zeit des Servers verwendet 
	 * @return die Anzahl der abgemeldeten Benutzer
	 * @throws EJBExceptionLP
	 */
	int logoutAllClients(boolean remove) throws EJBExceptionLP ;
	public void setRmiPort(TheClientDto theClientDto, Integer rmiPort);
	public List<VerfuegbareHostsDto> getVerfuegbareHosts(
			String benutzerCNr, String mandantCNr, TheClientDto theClientDto);
	
	public List<VerfuegbareHostsDto> getVerfuegbareHosts(Integer personalIId,
			TheClientDto theClientDto);

	/**
	 * Die Client-Daten zum angegebenen Token ermitteln.<br>
	 * <p>Das impliziert eine interne &Uuml;berpr&uuml;fung, ob der Client
	 * noch angemeldet ist, ber&uuml;cksichtigt auch, eventuelle 
	 * &Auml;nderungen der Systemrollen</p>
	 * 
	 * @param token
	 * @return die ClientDaten
	 */
	TheClientDto theClientFindByUserLoggedInMobil(String token);

	/**
	 * Die Liste aller Benutzer gegen den Mandantenzaehler, die "heute" angemeldet sind.</br>
	 * 
	 * @return eine (leere) Liste aller Benutzer, die heute als angemeldet
	 * z&auml;hlen. 
	 */
	List<TheClientLoggedInDto> getLoggedInClients();

	/**
	 * Die Anzahl der Benutzer die heute als angemeldete Benutzer an den  
	 * Mandanten zaehlen</br>
	 * <p>&Uuml;blicherweise sind das Desktop-Benutzer (und Restapi-Benutzer).
	 * Ein Benutzer kann vom gleichen Rechner aus eine beliebige Anzahl an
	 * Anmeldungen am HELIUM V Server haben. Er kann sich auch (vom gleichen 
	 * Rechner) an unterschiedlichen Mandanten anmelden. Meldet sich der
	 * gleiche Benutzer von unterschiedlichen Rechnern aus an, verbraucht er
	 * Concurrent User.</p> 
	 * @return Anzahl der angemeldeten Benutzer an Mandanten
	 */
	Integer getZaehlerAngemeldeteBenutzer();
	
	/**
	 * Die Anzahl jener HVMA-Benutzer die angemeldet sind.</br>
	 * <p>Bei den HVMA-Benutzer handelt es sich um benannte Benutzer. D.h.
	 * nur derjenige Benutzer der (zum Anmeldezeitpunkt) als Benutzer 
	 * f&uuml;r eine bestimmte Lizenz bekannt war, darf sich &uuml;berhaupt
	 * anmelden. Der Benutzer darf sich an beliebig vielen Ger&auml;ten
	 * gleichzeitig anmelden (zumindest vorerst). Es kann - und wird daher -
	 * passieren, dass die hier gelieferte Anzahl h&ouml;her ist, als 
	 * die in der Lizenz hinterlegte maximale Anzahl an Benutzer.</p>
	 * @param hvmalizenzId
	 * @return Anzahl der angemeldeten HVMA Benutzer dieser Lizenz
	 */
	Integer getZaehlerAngemeldeteHvmaBenutzer(Integer hvmalizenzId);

	Integer getZaehlerAngemeldeteBenutzerRolle(Integer systemrolleId);

	List<VerfuegbareHostsDto> getVerfuegbareBenutzer(String host, TheClientDto theClientDto);

	Integer getRmiPort(TheClientDto theClientDto);
}
