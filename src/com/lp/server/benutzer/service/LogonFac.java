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
package com.lp.server.benutzer.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import javax.ejb.Remote;

import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientLoggedInDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface LogonFac{
	public static String USERNAMEDELIMITER = "|";

	public static int IDX_BUILDNUMMERSERVER = 0;
	public static int IDX_BUILDNUMMERSERVERVON = 1;
	public static int IDX_BUILDNUMMERSERVERBIS = 2;

	public static int IDX_BUILDNUMMERCLIENT = 0;
	public static int IDX_BUILDNUMMERCLIENTVON = 1;
	public static int IDX_BUILDNUMMERCLIENTBIS = 2;

	public static int EINE_MINUTE_IN_MS = 60 * 1000;
	public static int EINE_STUNDE_IN_MS = 60 * EINE_MINUTE_IN_MS;
	public static int ZWEI_MINUTE_IN_MS = 120 * 1000;

	public static String JVM_VERSION = "1.5.0_08";
	
	public static final String LPWEBAPPZEMECS = "lpwebappzemecs";
	
	public static class  AppType {
		public static int Stueckliste =  1 ;
	}
	
	/**
	 * @param sFullUserNameI 
	 * @param cKennwortI 
	 * @param locUII 
	 * @param sMandantI 
	 * @param cNrUserI 
	 * @param tLogontimeI 
	 * @param bOhneBuildnummmerCheck 
	 * @return theClientDto.getIDUser()
	 * @throws EJBExceptionLP 
	 * @throws RemoteException 
	 * @deprecated use {@link #logon(String, char[], Locale, String, Timestamp)}.getIDUser()
	 */
	public String logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, String cNrUserI,
		    Timestamp tLogontimeI,boolean bOhneBuildnummmerCheck)
			throws EJBExceptionLP, RemoteException;

	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI,
			Integer iiBuildnummerClientI, Timestamp tLogontimeI)
			throws EJBExceptionLP;

	/**
	 * @param sFullUserNameI 
	 * @param cKennwortI 
	 * @param locUII 
	 * @param sMandantI 
	 * @param theClientDto 
	 * @param iiBuildnummerClientI 
	 * @param tLogontimeI 
	 * @return theClientDto.getIDUser()
	 * @throws EJBExceptionLP 
	 * @deprecated use {@link #logon(String, char[], Locale, String, Timestamp)}
	 */
	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, TheClientDto theClientDto,
			Integer iiBuildnummerClientI, Timestamp tLogontimeI)
				throws EJBExceptionLP;

	/**
	 * @param sFullUserNameI
	 * @param cKennwortI
	 * @param locUII
	 * @param sMandantI
	 * @param theClientDto
	 * @param tLogontimeClientI
	 * @return das TheClientDto
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 * @deprecated use {@link #logon(String, char[], Locale, String, Timestamp)}
	 */
	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, TheClientDto theClientDto,
			Timestamp tLogontimeClientI) throws EJBExceptionLP, RemoteException;
	
	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
					Locale locUII, String sMandantI, Timestamp tLogontimeClientI)
							throws EJBExceptionLP;

	public void logout(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public boolean changePassword(String benutzerCNr, char[] sOldKennwort,
			char[] sNewKennwort) throws EJBExceptionLP, RemoteException;

	public Integer getIBenutzerFrei(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	void validateLoggedIn(TheClientDto theClientDto) ;
	
	/**
	 * Anmeldung von externem Client</br>
	 * <p>Der externe Client wird auf einen HELIUM V Client projeziert. 
	 * Die Credentials werden im Ansprechpartner eines Kunden ermittelt.
	 * Es wird versucht das Verhalten eines HV-Client Logons zu simulieren. 
	 * </p> 
	 * @param appType der Applikationstype (anhand dessen kann entschieden werden,
	 *   welcher Benutzer verwendet wird)
	 * @param sFullUserNameI der Benutzername (E-Mail Adresse des Ansprechpartners).
	 *   Ist der Benutzername nicht vorhanden gibt es die Exception FEHLER_BEI_FINDBYPRIMARYKEY.
	 *   Wird die gleiche E-Mail Adresse mehrmals vergeben, gibt es FEHLER_NO_UNIQUE_RESULT.
	 * @param cKennwortI das im Ansprechpartner hinterlegte Kennwort
	 * @param uiLocale ist die Locale der UI mit der man sich anmelden moechte
	 * @param source ist ein beliebiger String beispielsweise die IP-Adresse des Web-Clients
	 * @return das TheClientDto
	 * @throws EJBExceptionLP
	 */
	TheClientDto logonExtern(int appType, String sFullUserNameI, char[] cKennwortI,
			Locale uiLocale, String source) throws EJBExceptionLP, RemoteException ;	
	
	TheClientDto logonExtern(int appType, String userName, char[] cKennwortI,
			Locale uiLocale, String mandantCnr, String source) throws EJBExceptionLP, RemoteException ; 
	
	TheClientDto logonMobil(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, Timestamp tLogontimeClientI, String cAusweis) throws EJBExceptionLP, RemoteException ;
	
	public TheClientDto logonIntern(Locale locUII, String sMandantI);

	TheClientDto logonHvma(String fullUsername, char[] kennwort, Locale locale,
			String mandantCnr, Timestamp logontime,
			HvmaLizenzEnum licence, String resource);
	
	List<TheClientLoggedInDto> getUsersLoggedInMandant(TheClientDto theClientDto) throws RemoteException;
}
