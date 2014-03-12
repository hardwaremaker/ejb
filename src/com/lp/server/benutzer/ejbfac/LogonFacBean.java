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
package com.lp.server.benutzer.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.benutzer.ejb.Benutzer;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.LocaleDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.ServiceLocator;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class LogonFacBean extends Facade implements LogonFac {

	@PersistenceContext
	private EntityManager em;

	public static final short MAX_FAILURES = 10;

	public static final String MESSAGE_USER_NOT_LOGGED_ON = "User not logged on.";
	public static final String MESSAGE_USER_NOT_FOUND = "User not found.";
	public static final String MESSAGE_WRONG_PASSWORD = "Wrong Password.";
	public static final String MESSAGE_USER_EXPIERED = "User expired.";
	public static final String MESSAGE_USER_LOCKED = "User locked.";
	public static final String MESSAGE_SPRACHE_NOT_FOUND = "Sprache not found.";
	public static final String MESSAGE_PROFIL_NOT_FOUND = "Profil not found.";
	public static final String MESSAGE_MANDANT_NOT_FOUND = "Mandant not found.";
	public static final String MESSAGE_TOO_MANY_FAILURES = "Too many Failures.";
	public static final String MESSAGE_LICENSE_UNSUIFFICIENT = "License insufficient.";
	public static final String INSTANCE_LIZENZMANGER_FAILED = "Instanzing Lizenzmanger failed";

	/**
	 * Am Server anmelden.
	 * 
	 * @param sFullUserNameI
	 *            String
	 * @param cKennwortI
	 *            char[]
	 * @param locUII
	 *            Locale
	 * @param sMandantI
	 *            String
	 * @param theClientDto der aktuelle Benutzer
	 * @param iiBuildnummerClientI
	 *            Integer
	 * @param tLogontimeI
	 *            Timestamp
	 * @return String
	 * @throws EJBExceptionLP
	 */
	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, TheClientDto theClientDto,
			Integer iiBuildnummerClientI, Timestamp tLogontimeI)
			throws EJBExceptionLP {

		// Check Buildnummer
		checkBuildnumbers(iiBuildnummerClientI);

		return logon(sFullUserNameI, cKennwortI, locUII, sMandantI,
				theClientDto, tLogontimeI);
	}

	public String logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, String cNrUserI,
			Integer iiBuildnummerClientI, Timestamp tLogontimeI)
			throws EJBExceptionLP {

		// Check Buildnummer
		checkBuildnumbers(iiBuildnummerClientI);

		return logon(sFullUserNameI, cKennwortI, locUII, sMandantI, cNrUserI,
				tLogontimeI);
	}

	public String logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, String cNrUserI,
			Timestamp tLogontimeI, boolean b) throws EJBExceptionLP {

		return logon(sFullUserNameI, cKennwortI, locUII, sMandantI, cNrUserI,
				tLogontimeI);
	}

	private AnwenderDto checkBuildnumbers(Integer iBuildnummerClientI)
			throws EJBExceptionLP {

		// Server gegen DB
		int iBuildnummerServer = Integer.parseInt(HelperServer
				.getLPResourceBundle().getString("lp.version.server.build"));
		AnwenderDto anwenderDto = null;
		try {
			anwenderDto = getSystemFac().anwenderFindByPrimaryKey(
					new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		if (iBuildnummerServer < anwenderDto.getIBuildnummerServerVon()
				.intValue()
				|| iBuildnummerServer > anwenderDto.getIBuildnummerServerBis()
						.intValue()) {
			ArrayList<Object> al = new ArrayList<Object>(3);
			al.add(LogonFac.IDX_BUILDNUMMERSERVER, new Integer(
					iBuildnummerServer));
			al.add(LogonFac.IDX_BUILDNUMMERSERVERVON,
					anwenderDto.getIBuildnummerServerVon());
			al.add(LogonFac.IDX_BUILDNUMMERSERVERBIS,
					anwenderDto.getIBuildnummerServerBis());
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BUILDNUMMER_SERVER_DB, al, null);
		}

		try {
			// CK -> Ab Buildnummer 3414 funktioniert das auto. Client-Update
			if (getTheClientFac().istNeuerClientVerfuegbar(iBuildnummerClientI)
					&& iBuildnummerClientI >= 3414) {
			} else {
				// Client gegen Server
				if (iBuildnummerClientI.intValue() < anwenderDto
						.getIBuildnummerClienVon().intValue()
						|| iBuildnummerClientI.intValue() > anwenderDto
								.getIBuildnummerClientBis().intValue()) {
					ArrayList<Object> al = new ArrayList<Object>(3);
					al.add(LogonFac.IDX_BUILDNUMMERCLIENT, new Integer(
							iBuildnummerClientI.intValue()));
					al.add(LogonFac.IDX_BUILDNUMMERCLIENTVON,
							anwenderDto.getIBuildnummerClienVon());
					al.add(LogonFac.IDX_BUILDNUMMERCLIENTBIS,
							anwenderDto.getIBuildnummerClientBis());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BUILDNUMMER_CLIENT_SERVER,
							al, null);
				}
			}
		} catch (RemoteException ex1) {
			throwEJBExceptionLPRespectOld(ex1);
		}

		return anwenderDto;
	}

	/**
	 * Am Server anmelden.
	 * 
	 * @param sFullUserNameI
	 *            String
	 * @param cKennwortI
	 *            char[]
	 * @param locUII
	 *            Locale
	 * @param sMandantI
	 *            String
	 * @param theClientDto der aktuelle Benutzer 
	 * @param tLogontimeClientI
	 *            Timestamp
	 * @return String
	 * @throws EJBExceptionLP
	 */

	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, TheClientDto theClientDto,
			Timestamp tLogontimeClientI) throws EJBExceptionLP {

		int iUserzahl = getUseranzahl();

		// Precondition
		theClientDto = new TheClientDto();

		StringTokenizer st = new StringTokenizer(sFullUserNameI,
				LogonFac.USERNAMEDELIMITER);
		String user = st.nextToken();
		String cNrUser = null;
		BenutzerDto benutzerDto = null;
		String mandantenSprache = null;

		// wird nur hier 1x pro client angelegt!
		// an den client durchgereicht und bei jedem serveraufruf mitgegeben
		PKGeneratorObj pk = new PKGeneratorObj();
		cNrUser = pk.getNextPrimaryKey(PKConst.PK_LOGIN) + sFullUserNameI;
		TheClientFac theClientFac = getTheClientFac();

		try {
			benutzerDto = getBenutzerFac().benutzerFindByCBenutzerkennung(user,
					new String(cKennwortI));
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		if (Helper.short2boolean(benutzerDto.getBGesperrt())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_IST_GESPERRT, "");
		}
		if (benutzerDto.getTGueltigbis() != null) {
			if (benutzerDto.getTGueltigbis().before(
					new java.sql.Timestamp(System.currentTimeMillis()))) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG,
						"");
			}
		}

		try {
			// Darf sich Benutzer bei bei irgendeinem Mandanten anmelden?
			BenutzermandantsystemrolleDto[] dtos = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIId(
							benutzerDto.getIId());
			if (dtos == null || dtos.length < 1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT,
						new Exception("dtos==null || dtos.length <1"));

			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN,
					new Exception(benutzerDto.getMandantCNrDefault()));
		}

		try {
			// Darf sich Benutzer bei Mandant XXX anmelden?
			BenutzermandantsystemrolleDto benutzermandansystemrolleDto = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIIdMandantCNr(
							benutzerDto.getIId(),
							sMandantI != null ? sMandantI : benutzerDto
									.getMandantCNrDefault());
			theClientDto.setIDPersonal(benutzermandansystemrolleDto
					.getPersonalIIdZugeordnet());
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN,
					new Exception(benutzerDto.getMandantCNrDefault()));
		}

		theClientDto.setIDUser(cNrUser);
		theClientDto.setBenutzername(sFullUserNameI);
		theClientDto.setKennwort(cKennwortI);
		theClientDto.setMandant(sMandantI != null ? sMandantI : benutzerDto
				.getMandantCNrDefault());

		try {
			// Konzernlocale = Locale des Hauptmandanten
			MandantFac mandantFac = getMandantFac();
			SystemFac systemFac = getSystemFac();
			AnwenderDto anwenderDto = systemFac
					.anwenderFindByPrimaryKey(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER);

			// AD: PJ 15338 Ablaufdatum
			if (anwenderDto.getTAblauf() != null) {
				if (anwenderDto.getTAblauf().before(
						new Timestamp(System.currentTimeMillis()))) {
					EJBExceptionLP en = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_LIZENZ_ABGELAUFEN,
							"Lizenz abgelaufen!");
					throw en;
				}
			}

			MandantDto mandantDto = mandantFac.mandantFindByPrimaryKey(
					anwenderDto.getMandantCNrHauptmandant(), theClientDto);
			theClientDto.setLocKonzern(Helper.string2Locale(mandantDto
					.getPartnerDto().getLocaleCNrKommunikation()));

			mandantDto = getMandantFac().mandantFindByPrimaryKey(
					sMandantI != null ? sMandantI
							: benutzerDto.getMandantCNrDefault(), theClientDto);

			// AD: Projekt 3640 User zaehlen
			if ((sFullUserNameI.compareTo("lpwebappzemecs") != 0)
					&& ((mandantDto.getIBenutzermax() == null) || (mandantDto
							.getIBenutzermax().intValue() < iUserzahl + 1))) {
				if (getTheClientFac().theClientFindByCBenutzernameLoggedIn(
						theClientDto.getBenutzername()) == null) {
					// noch nicht angemeldet
					EJBExceptionLP en = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_MAXIMALE_BENUTZERANZAHL_UEBERSCHRITTEN,
							"");
					ArrayList<Object> alInfo = new ArrayList<Object>();
					alInfo.add(mandantDto.getIBenutzermax());
					alInfo.add(new Integer(iUserzahl));
					en.setAlInfoForTheClient(alInfo);
					throw en;
				}
			}

			// CK: Projekt 8440
			mandantenSprache = mandantDto.getPartnerDto()
					.getLocaleCNrKommunikation();
			theClientDto.setLocMandant(Helper.string2Locale(mandantenSprache));
			theClientDto.setSMandantenwaehrung(mandantDto.getWaehrungCNr());
			theClientDto.setUiLoc(locUII);

			// CK Projekt 7949 Darf sich der Benutzer in dieser Sprache
			// anmelden?
			ZusatzfunktionberechtigungDto zusatzfunktionberechtigungDto = getMandantFac()
					.zusatzfunktionberechtigungFindByPrimaryKey(
							MandantFac.ZUSATZFUNKTION_MEHRSPRACHIGKEIT,
							benutzerDto.getMandantCNrDefault());
			if (zusatzfunktionberechtigungDto == null
					&& !locUII.equals(Helper.string2Locale(mandantDto
							.getPartnerDto().getLocaleCNrKommunikation()))) {
				ArrayList<Object> alEx = new ArrayList<Object>();
				alEx.add(Helper.string2Locale(mandantDto.getPartnerDto()
						.getLocaleCNrKommunikation()));
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_IN_DIESER_SPRACHE_NICHT_ANMELDEN,
						alEx, new Exception(mandantDto.getPartnerDto()
								.getLocaleCNrKommunikation()));

			}

			// CK 11378 Darf sich Benutzer bei dieser Sprache anmelden (Wenn
			// B_AKTIV = 1)
			try {
				LocaleDto localeDto = getLocaleFac()
						.localeFindByPrimaryKeyOhneExc(
								Helper.locale2String(locUII));

				if (localeDto != null
						&& Helper.short2Boolean(localeDto.getBAktiv()) == false) {
					ArrayList<Object> alEx = new ArrayList<Object>();
					alEx.add(locUII);
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_SPRACHE_NICHT_AKTIV, alEx,
							new Exception("Sprache "
									+ Helper.locale2String(locUII)
									+ " nicht aktiv"));

				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			// CK: -4 Millisekounden, da MS-SQL-Server nur auf 3 ms genau
			// aufloest.
			theClientDto.setTsLoggedin(new java.sql.Timestamp(System
					.currentTimeMillis() - 4));

			theClientFac.createTheClient(theClientDto);

			loescheAlteLogons(theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// Uhrzeitcheck.
		long lServer = System.currentTimeMillis();
		long lDiff = (Math.abs(lServer - tLogontimeClientI.getTime()) % LogonFac.EINE_STUNDE_IN_MS);
		if (lDiff > LogonFac.ZWEI_MINUTE_IN_MS) {
			myLogger.logKritisch("|Minutenclientuhrzeit - Minutenserveruhrzeit| > 1 min; : user:"
					+ user + " " + lDiff + "[ms]");
		}

		/*
		 * // JVM Check: MR:bei JDK 1.6.0_05 ist java.vm.version 10.0-b19 die
		 * java.version aber richtig 1.6.0_05 String sJVM =
		 * System.getProperty("java.version"); String tmp = sJVM.substring(0,
		 * 8); sJVM = tmp; if (!LogonFac.JVM_VERSION.equals(sJVM)) { String sM =
		 * "Installed: " + sJVM + " To use: " + LogonFac.JVM_VERSION + "; " +
		 * getTextRespectUISpr("lp.jvm.version", theClientDto.getMandant(),
		 * theClientDto.getLocUi()); myLogger.logKritisch(sM);
		 * theClientDto.setIStatus(new
		 * Integer(EJBExceptionLP.WARNUNG_SERVER_JVM)); try {
		 * getTheClientFac().updateTheClient(theClientDto); } catch
		 * (RemoteException ex) { throwEJBExceptionLPRespectOld(ex); } }
		 */

		myLogger.logData("logged in > usr: " + theClientDto.getIDUser());
		return theClientDto;
	}

	public String logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, String cNrUserI,
			Timestamp tLogontimeClientI) throws EJBExceptionLP {

		myLogger.logData("try logon > usr: " + cNrUserI);

		int iUserzahl = getUseranzahl();

		// Precondition
		if (sMandantI != null) {
			// Erneutes Logon mit Mandantwechsel.
			if (cNrUserI == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"cNrUserI == null"));
			}
		}

		StringTokenizer st = new StringTokenizer(sFullUserNameI,
				LogonFac.USERNAMEDELIMITER);
		String user = st.nextToken();
		String cNrUser = null;
		BenutzerDto benutzerDto = null;
		String mandantenSprache = null;

		// wird nur hier 1x pro client angelegt!
		// an den client durchgereicht und bei jedem serveraufruf mitgegeben
		PKGeneratorObj pk = new PKGeneratorObj();
		cNrUser = pk.getNextPrimaryKey(PKConst.PK_LOGIN) + sFullUserNameI;
		TheClientFac theClientFac = getTheClientFac();

		try {
			benutzerDto = getBenutzerFac().benutzerFindByCBenutzerkennung(user,
					new String(cKennwortI));
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		if (Helper.short2boolean(benutzerDto.getBGesperrt())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_IST_GESPERRT, "");
		}
		if (benutzerDto.getTGueltigbis() != null) {
			if (benutzerDto.getTGueltigbis().before(
					new java.sql.Timestamp(System.currentTimeMillis()))) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG,
						"");
			}
		}

		try {
			// Darf sich Benutzer bei bei irgendeinem Mandanten anmelden?
			BenutzermandantsystemrolleDto[] dtos = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIId(
							benutzerDto.getIId());
			if (dtos == null || dtos.length < 1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT,
						new Exception("dtos==null || dtos.length <1"));

			}

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN,
					new Exception(benutzerDto.getMandantCNrDefault()));
		}

		TheClientDto theClientDto = new TheClientDto();

		try {
			// Darf sich Benutzer bei Mandant XXX anmelden?
			BenutzermandantsystemrolleDto benutzermandansystemrolleDto = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIIdMandantCNr(
							benutzerDto.getIId(),
							sMandantI != null ? sMandantI : benutzerDto
									.getMandantCNrDefault());
			theClientDto.setIDPersonal(benutzermandansystemrolleDto
					.getPersonalIIdZugeordnet());
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN,
					new Exception(benutzerDto.getMandantCNrDefault()));
		}

		theClientDto.setIDUser(cNrUser);
		theClientDto.setBenutzername(sFullUserNameI);
		theClientDto.setKennwort(cKennwortI);
		theClientDto.setMandant(sMandantI != null ? sMandantI : benutzerDto
				.getMandantCNrDefault());

		try {
			// Konzernlocale = Locale des Hauptmandanten
			MandantFac mandantFac = getMandantFac();
			SystemFac systemFac = getSystemFac();
			AnwenderDto anwenderDto = systemFac
					.anwenderFindByPrimaryKey(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER);
			MandantDto mandantDto = mandantFac.mandantFindByPrimaryKey(
					anwenderDto.getMandantCNrHauptmandant(), theClientDto);
			theClientDto.setLocKonzern(Helper.string2Locale(mandantDto
					.getPartnerDto().getLocaleCNrKommunikation()));

			mandantDto = getMandantFac().mandantFindByPrimaryKey(
					sMandantI != null ? sMandantI
							: benutzerDto.getMandantCNrDefault(), theClientDto);

			// AD: Projekt 3640 User zaehlen
			if ((sFullUserNameI.compareTo("lpwebappzemecs") != 0)
					&& ((mandantDto.getIBenutzermax() == null) || (mandantDto
							.getIBenutzermax().intValue() <= iUserzahl))) {
				EJBExceptionLP en = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_MAXIMALE_BENUTZERANZAHL_UEBERSCHRITTEN,
						"");
				ArrayList<Object> alInfo = new ArrayList<Object>();
				alInfo.add(mandantDto.getIBenutzermax());
				alInfo.add(new Integer(iUserzahl));
				en.setAlInfoForTheClient(alInfo);
				throw en;
			}

			// CK: Projekt 8440
			mandantenSprache = mandantDto.getPartnerDto()
					.getLocaleCNrKommunikation();
			theClientDto.setLocMandant(Helper.string2Locale(mandantenSprache));
			theClientDto.setSMandantenwaehrung(mandantDto.getWaehrungCNr());
			theClientDto.setUiLoc(locUII);

			// CK Projekt 7949 Darf sich der Benutzer in dieser Sprache
			// anmelden?
			ZusatzfunktionberechtigungDto zusatzfunktionberechtigungDto = getMandantFac()
					.zusatzfunktionberechtigungFindByPrimaryKey(
							MandantFac.ZUSATZFUNKTION_MEHRSPRACHIGKEIT,
							benutzerDto.getMandantCNrDefault());
			if (zusatzfunktionberechtigungDto == null
					&& !locUII.equals(Helper.string2Locale(mandantDto
							.getPartnerDto().getLocaleCNrKommunikation()))) {
				ArrayList<Object> alEx = new ArrayList<Object>();
				alEx.add(Helper.string2Locale(mandantDto.getPartnerDto()
						.getLocaleCNrKommunikation()));
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_IN_DIESER_SPRACHE_NICHT_ANMELDEN,
						alEx, new Exception(mandantDto.getPartnerDto()
								.getLocaleCNrKommunikation()));

			}

			// CK 11378 Darf sich Benutzer bei dieser Sprache anmelden (Wenn
			// B_AKTIV = 1)
			try {
				LocaleDto localeDto = getLocaleFac()
						.localeFindByPrimaryKeyOhneExc(
								Helper.locale2String(locUII));

				if (localeDto != null
						&& Helper.short2Boolean(localeDto.getBAktiv()) == false) {
					ArrayList<Object> alEx = new ArrayList<Object>();
					alEx.add(locUII);
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_SPRACHE_NICHT_AKTIV, alEx,
							new Exception("Sprache "
									+ Helper.locale2String(locUII)
									+ " nicht aktiv"));

				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			// CK: -4 Millisekounden, da MS-SQL-Server nur auf 3 ms genau
			// aufloest.
			theClientDto.setTsLoggedin(new java.sql.Timestamp(System
					.currentTimeMillis() - 4));

			theClientFac.createTheClient(theClientDto);

			loescheAlteLogons(theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		// Uhrzeitcheck.
		long lServer = System.currentTimeMillis();
		long lDiff = (Math.abs(lServer - tLogontimeClientI.getTime()) % LogonFac.EINE_STUNDE_IN_MS);
		if (lDiff > LogonFac.ZWEI_MINUTE_IN_MS) {
			myLogger.logKritisch("|Minutenclientuhrzeit - Minutenserveruhrzeit| > 1 min; : user:"
					+ user + " " + lDiff + "[ms]");
		}

		/*
		 * // JVM Check: MR:bei JDK 1.6.0_05 ist java.vm.version 10.0-b19 die
		 * java.version aber richtig 1.6.0_05 String sJVM =
		 * System.getProperty("java.version"); String tmp = sJVM.substring(0,
		 * 8); sJVM = tmp; if (!LogonFac.JVM_VERSION.equals(sJVM)) { String sM =
		 * "Installed: " + sJVM + " To use: " + LogonFac.JVM_VERSION + "; " +
		 * getTextRespectUISpr("lp.jvm.version", theClientDto.getMandant(),
		 * theClientDto.getLocUi()); myLogger.logKritisch(sM);
		 * theClientDto.setIStatus(new
		 * Integer(EJBExceptionLP.WARNUNG_SERVER_JVM)); try {
		 * getTheClientFac().updateTheClient(theClientDto); } catch
		 * (RemoteException ex) { throwEJBExceptionLPRespectOld(ex); } }
		 */

		myLogger.logData("logged in > usr: " + cNrUserI);
		return cNrUser;
	}

	private void loescheAlteLogons(TheClientDto theClientDto)
			throws RemoteException {
		Integer iTage = null;

		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_LOGON_LOESCHEN);

			iTage = new Integer(parameter.getCWert());
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		// CK 3 Alte Logons loeschen
		Session session = FLRSessionFactory.getFactory().openSession();
		// PJ 15986
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(c.DAY_OF_MONTH, c.get(c.DAY_OF_MONTH) - iTage.intValue());
		String hql = "delete from FLRTheClient where t_loggedin < :zeitpunkt";
		org.hibernate.Query query = session.createQuery(hql);
		query.setDate("zeitpunkt", c.getTime());
		int rowCount = query.executeUpdate();
		// System.out.println(String.format("%d Login-Eintraege geloescht",rowCount));
	}

	/**
	 * logout
	 * 
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */

	public void logout(TheClientDto theClientDto) throws EJBExceptionLP {
		ServiceLocator locator = ServiceLocator.getInstance();
		try {
			TheClientFac theClient = getTheClientFac();

			theClientDto.setTsLoggedout(new Timestamp(System
					.currentTimeMillis()));
			theClient.updateTheClient(theClientDto);
			getFastLaneReader().cleanup(null, null, theClientDto);
		} catch (RemoteException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
		}

	}

	/**
	 * simply changes the password - which is not achievable for the client
	 * 
	 * @param benutzerCNr
	 *            String
	 * @param sOldKennwort
	 *            char[]
	 * @param sNewKennwort
	 *            char[]
	 * @return boolean
	 * @throws EJBExceptionLP
	 */
	public boolean changePassword(String benutzerCNr, char[] sOldKennwort,
			char[] sNewKennwort) throws EJBExceptionLP {

		// find benutzer
		/*
		 * BenutzerObj benutzerObj = null; try { benutzerObj =
		 * getValidUser(benutzerCNr, sOldKennwort); } catch (Exception ex) {
		 * throw new EJBExceptionLP("logon", new GregorianCalendar(),
		 * ex.toString()); } // user should be OK now:
		 * benutzerObj.setCKennwort(sNewKennwort.toString());
		 */
		return true;
	}

	private int getUseranzahl() {
		// AD
		// return 0;
		Query q = em.createNativeQuery("SELECT I_ANZAHL FROM LP_USERCOUNT "
				+ "WHERE T_ZEITPUNKT >= ?1 " + "ORDER BY T_ZEITPUNKT DESC");
		q.setMaxResults(1);
		q.setParameter(1,
				Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
		try {
//			Integer anzahl = (Integer) ((List) q.getResultList()).get(0);
//			return anzahl.intValue();
			/* 
			 * getResultList() kann auch null liefern
			 */
			List userList =  q.getResultList() ;
			if(null == userList) return 0 ;
			return userList.size() > 0 ? (Integer) userList.get(0) : 0 ;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Integer getIBenutzerFrei(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		MandantFac mandantFac = getMandantFac();
		SystemFac systemFac = getSystemFac();
		AnwenderDto anwenderDto = systemFac
				.anwenderFindByPrimaryKey(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER);
		MandantDto mandantDto = mandantFac.mandantFindByPrimaryKey(
				anwenderDto.getMandantCNrHauptmandant(), theClientDto);
		int userangemeldet = getUseranzahl();
		int userfrei = 0;
		if (mandantDto.getIBenutzermax() != null) {
			userfrei = mandantDto.getIBenutzermax().intValue() - userangemeldet;
		}
		return new Integer(userfrei);
	}

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @return int
	 */
	public int hashCode() {
		return 0;
	}

	public void validateLoggedIn(TheClientDto theClientDto) {
		Validator.notNull(theClientDto, "theClientDto") ;
		Validator.notEmpty(theClientDto.getIDUser(), "idUser");
		Validator.notEmpty(theClientDto.getBenutzername(), "benutzername") ;
		
		try {
			Query query = em.createNamedQuery("BenutzerfindByCBenutzerkennung");
			query.setParameter(1, theClientDto.getBenutzername().trim());
			Benutzer benutzer = (Benutzer) query.getSingleResult() ;
			
			validateBenutzerLogon(benutzer, theClientDto.getMandant()) ;
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT,
					new Exception("not found"));
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT,
					new Exception("not unique"));
		}
	}
	
	private Integer validateBenutzerLogon(Benutzer benutzer, String mandant) {
		Integer personalId = null ;
		
		if(Helper.short2boolean(benutzer.getBGesperrt())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_IST_GESPERRT, "");				
		}

		if (benutzer.getTGueltigbis() != null) {
			if (benutzer.getTGueltigbis().before(
					new java.sql.Timestamp(System.currentTimeMillis()))) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG,
						"");
			}
		}		

		try {
			// Darf sich Benutzer bei bei irgendeinem Mandanten anmelden?
			BenutzermandantsystemrolleDto[] dtos = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIId(benutzer.getIId());
			if (dtos == null || dtos.length < 1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT,
						new Exception("dtos==null || dtos.length <1"));
			}
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN,
					new Exception(benutzer.getMandantCNrDefault()));
		}

		try {
			// Darf sich Benutzer beim angegebenen Mandanten anmelden?
			BenutzermandantsystemrolleDto benutzermandansystemrolleDto = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIIdMandantCNr(
							benutzer.getIId(),
							mandant != null ? mandant : benutzer
									.getMandantCNrDefault());
			personalId = benutzermandansystemrolleDto.getPersonalIIdZugeordnet() ;
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN,
					new Exception(benutzer.getMandantCNrDefault()));
		}

		return personalId ;
	}
}
