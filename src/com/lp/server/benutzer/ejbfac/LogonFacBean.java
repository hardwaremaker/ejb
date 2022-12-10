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
package com.lp.server.benutzer.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.benutzer.ejb.Benutzer;
import com.lp.server.benutzer.ejb.Systemrolle;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.partner.ejb.Ansprechpartner;
import com.lp.server.partner.ejb.AnsprechpartnerQuery;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.KundeQuery;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmabenutzerDto;
import com.lp.server.personal.service.HvmalizenzDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.ejb.LpUserCount;
import com.lp.server.system.ejb.Theclient;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
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
import com.lp.server.system.service.TheClientLoggedInDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
import com.lp.server.util.Facade;
import com.lp.server.util.ServerConfiguration;
import com.lp.server.util.ServiceLocator;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
//@RemoteBinding(jndiBinding="LogonFacXXX")
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
	 * @param iiBuildnummerClientI
	 *            Integer
	 * @param tLogontimeI
	 *            Timestamp
	 * @return String
	 * @throws EJBExceptionLP
	 */
	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI,
			Integer iiBuildnummerClientI, Timestamp tLogontimeI)
			throws EJBExceptionLP {

		// Check Buildnummer
		checkBuildnumbers(iiBuildnummerClientI);

		return logon(sFullUserNameI, cKennwortI, locUII, sMandantI, tLogontimeI);
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
	 * @param iiBuildnummerClientI
	 *            Integer
	 * @param tLogontimeI
	 *            Timestamp
	 * @return String
	 * @throws EJBExceptionLP
	 * @deprecated use {@link #logon(String, char[], Locale, String, Timestamp)}
	 */
	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, TheClientDto theClientDto,
			Integer iiBuildnummerClientI, Timestamp tLogontimeI)
			throws EJBExceptionLP {
		return logon(sFullUserNameI, cKennwortI, locUII, sMandantI, iiBuildnummerClientI, tLogontimeI);
	}



	/**
	 * @deprecated use {@link #logon(String, char[], Locale, String, Timestamp)}.getIDUser()
	 */
	public String logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, String cNrUserI,
			Timestamp tLogontimeI, boolean b) throws EJBExceptionLP {

		return logon(sFullUserNameI, cKennwortI, locUII, sMandantI, tLogontimeI).getIDUser();
	}

	private AnwenderDto checkBuildnumbers(Integer iBuildnummerClientI)
			throws EJBExceptionLP {

		// Server gegen DB
		int iBuildnummerServer = ServerConfiguration.getBuildNumber() ;
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

	@Override
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
	 * @deprecated use {@link #logon(String, char[], Locale, String, Timestamp)}
	 */
	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, TheClientDto theClientDto,
			Timestamp tLogontimeClientI) throws EJBExceptionLP {
		return logon(sFullUserNameI, cKennwortI, locUII, sMandantI, tLogontimeClientI);
	}
	

	private void validateLogonBenutzerDto(BenutzerDto benutzerDto) {
		if (Helper.isTrue(benutzerDto.getBGesperrt())) {
			throw new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BENUTZER_IST_GESPERRT, 
				benutzerDto.getCBenutzerkennung());
		}
		
		if (benutzerDto.getTGueltigbis() != null) {
			if (benutzerDto.getTGueltigbis().before(getTimestamp())) {
				throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG,
					benutzerDto.getCBenutzerkennung());
			}
		}
	}
	
	private void validateBenutzerAnyMandant(BenutzerDto benutzerDto) {
		try {
			BenutzermandantsystemrolleDto[] dtos = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIId(
							benutzerDto.getIId());
			if (dtos == null || dtos.length < 1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BENUTZER_KEIN_EINTRAG_IN_BENUTZERMANDANT,
						new Exception("Kein Eintrag in Benutzermandant fuer Benutzer '" + benutzerDto.getCBenutzerkennung() + "'"));
			}
		} catch (RemoteException ex) {
			// TODO: Die Meldung stimmt so nicht, es gab eine RemoteException,
			// hat aber nichts damit zu tun, dass an diesem Mandanten nicht erlaubt
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN,
					new Exception(benutzerDto.getMandantCNrDefault()));
		}
	}
	
	private String createHvmaResource() {
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HVMARESOURCE);
		return pk.toString();
	}
	
	private TheClientDto updateClientDtoFrom(BenutzerDto benutzerDto, 
			String mandantCnr, HvmaLizenzEnum licence, String resource, TheClientDto theClientDto) {
		try {
			// Darf sich Benutzer bei Mandant anmelden?
			BenutzermandantsystemrolleDto bmsrDto = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIIdMandantCNr(
							benutzerDto.getIId(), mandantCnr);
			theClientDto.setIDPersonal(bmsrDto.getPersonalIIdZugeordnet());
			theClientDto.setMandant(mandantCnr);

			if(licence == null) {
				return theClientDto;
			}
			
			HvmalizenzDto lizenzDto = getHvmaFac().hvmalizenzFindByEnum(licence);
			if(lizenzDto.getIMaxUser() == 0) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_HVMABENUTZER_ANZAHL_UEBERSCHRITTEN, licence.getText());
			}
			
			HvmabenutzerDto hvmaBenutzerDto = getHvmaFac()
					.hvmabenutzerFindByBenutzerIdLizenzId(
							benutzerDto.getIId(), lizenzDto.getIId());

			if(bmsrDto.getSystemrolleIIdHvma() == null) {
				throw EJBExcFactory.systemRolleHvmaFehlt(
						benutzerDto.getCBenutzerkennung(),
						bmsrDto.getSystemrolleIId());
			}
			theClientDto.setHvmaLizenzId(hvmaBenutzerDto.getHvmalizenzIId());
			
			if(Helper.isStringEmpty(resource)) {
				resource = createHvmaResource();
			}
			theClientDto.setHvmaResource(resource);
			return theClientDto;
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN,
					new Exception(benutzerDto.getMandantCNrDefault()));
		}		
	}

	private String getBenutzerFromFullUsername(String fullusername) {
		StringTokenizer st = new StringTokenizer(fullusername,
				LogonFac.USERNAMEDELIMITER);
		return st.nextToken();
	}
	
	private TheClientDto logonImpl(String sFullUserNameI,
			char[] cKennwortI, Locale locUII, String sMandantI, 
			Timestamp tLogontimeClientI, boolean desktopBenutzer,
			HvmaLizenzEnum licence, String resource) {
		myLogger.logData("try logon > usr: " + sFullUserNameI);
		
		String user = getBenutzerFromFullUsername(sFullUserNameI);
		BenutzerDto benutzerDto = null;
		
		TheClientDto theClientDto = new TheClientDto();
		theClientDto.setDesktopBenutzer(desktopBenutzer);

		for(int i = 0; i < 5; i++) {
			theClientDto.setIDUser(UUID.randomUUID().toString());
			if(getTheClientFac().theClientFindByPrimaryKeyOhneExc(
					theClientDto.getIDUser()) == null) break;
			//TODO: (rk) nach dem 5. mal sollte wohl eine Exception kommen,
			//jedoch ist es sehr unwahrscheinlich, dass hier ueberhaupt eine Kollision auftritt.
		}
		try {
			benutzerDto = getBenutzerFac()
					.benutzerFindByCBenutzerkennung(user, new String(cKennwortI));
			validateLogonBenutzerDto(benutzerDto);
			validateBenutzerAnyMandant(benutzerDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		if(sMandantI == null) {
			sMandantI = benutzerDto.getMandantCNrDefault();
		}
		updateClientDtoFrom(benutzerDto, 
				sMandantI, licence, resource, theClientDto);
		theClientDto.setBenutzername(sFullUserNameI);
		theClientDto.setKennwort(cKennwortI);

		try {
			AnwenderDto anwenderDto = getSystemFac()
					.anwenderFindByPrimaryKey(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER);

			if(anwenderDto.getTAblauf() != null && anwenderDto.getTAblauf().before(getTimestamp())) {
				SimpleDateFormat format = new SimpleDateFormat();
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIZENZ_ABGELAUFEN,
						"Lizenz abgelaufen", format.format(anwenderDto.getTAblauf()));
			}
			
			getBenutzerServicesFac().reloadRolleRechte();
			
			// Konzernlocale = Locale des Hauptmandanten
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					anwenderDto.getMandantCNrHauptmandant(), theClientDto);
			theClientDto.setLocKonzern(Helper.string2Locale(mandantDto
					.getPartnerDto().getLocaleCNrKommunikation()));

			mandantDto = getMandantFac().mandantFindByPrimaryKey(sMandantI, theClientDto);

			theClientDto.setSystemrolleIId(getTheJudgeFac().getSystemrolleIId(theClientDto));
			
			if (getTheClientFac().theClientFindByCBenutzernameLoggedIn(
					theClientDto.getBenutzername()) == null) {
				int frei = getIBenutzerFrei(theClientDto);
				// AD: Projekt 3640 User zaehlen
				
//				if (!user.equals(LPWEBAPPZEMECS) && 
				if(frei <= 0) {
					EJBExceptionLP en = new EJBExceptionLP(
							EJBExceptionLP.FEHLER_MAXIMALE_BENUTZERANZAHL_UEBERSCHRITTEN,
							"");
					ArrayList<Object> alInfo = new ArrayList<Object>();
					alInfo.add(mandantDto.getIBenutzermax());
					en.setAlInfoForTheClient(alInfo);
					throw en;
				}
			}

			// CK: Projekt 8440
			String mandantenSprache = mandantDto.getPartnerDto()
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

			loescheAlteLogons(theClientDto);
			
			getTheClientFac().createTheClient(theClientDto);
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
		myLogger.logData("logged in > usr: " + theClientDto.getIDUser());
		return theClientDto;		
	}
	
	@Override
	public TheClientDto logon(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, Timestamp tLogontimeClientI)
			throws EJBExceptionLP {
		if(sFullUserNameI.contains(LPWEBAPPZEMECS)){
			myLogger.warn("Anmeldung mit " + LPWEBAPPZEMECS +
					" sollte ueber logonIntern() erfolgen");
		}
		return logonImpl(sFullUserNameI, cKennwortI,
				locUII, sMandantI, tLogontimeClientI, true, null, null) ;
	}

	@Override
	public TheClientDto logonMobil(String sFullUserNameI, char[] cKennwortI,
			Locale locUII, String sMandantI, Timestamp tLogontimeClientI, String cAusweis)
			throws EJBExceptionLP, RemoteException {
		if (cAusweis == null) {
			return logonImpl(sFullUserNameI, cKennwortI, locUII,
					sMandantI, tLogontimeClientI, false, null, null) ;
		}
		
		PersonalDto personalDto = getPersonalFac().personalFindByCAusweis(cAusweis);
		if (personalDto == null || !personalDto.getMandantCNr().equals(sMandantI)) {
			return null;
		}
		
		TheClientDto theClientDto = logonImpl(sFullUserNameI, cKennwortI,
				locUII, sMandantI, tLogontimeClientI, false, null, null) ;
		theClientDto.setIDPersonal(personalDto.getIId()); 
		getTheClientFac().updateTheClient(theClientDto);
		
		return theClientDto;
	}
	
	@Override
	public TheClientDto logonHvma(String fullUsername, char[] kennwort,
			Locale locale, String mandantCnr, Timestamp logontime, 
			HvmaLizenzEnum licence, String resource) {
		return logonImpl(fullUsername, kennwort, locale, 
				mandantCnr, logontime, false, licence, resource) ;
	}
	
	private void loescheAlteLogons(TheClientDto theClientDto)
			throws RemoteException {
		Integer iTage = null;

		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_LOGON_LOESCHEN);
			iTage = parameter.asInteger();
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		// CK 3 Alte Logons loeschen
		Session session = getNewSession();
		// PJ 15986
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - iTage.intValue());
		// PJ20611 HVMA Logons werden nicht entfernt
		String hql = "delete from FLRTheClient where "
				+ "hvmalizenz_IId is NULL AND "
				+ "t_loggedin < :zeitpunkt ";
		org.hibernate.Query query = session.createQuery(hql);
		query.setDate("zeitpunkt", c.getTime());
		query.executeUpdate();
	}

	/**
	 * logout
	 * 
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */

	public void logout(TheClientDto theClientDto) throws EJBExceptionLP {
		ServiceLocator.getInstance();
		try {
			theClientDto.setTsLoggedout(new Timestamp(System
					.currentTimeMillis()));
			getTheClientFac().updateTheClient(theClientDto);
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
	
	/**
	 * Ermittelt die Anzahl der Angemeldeten User im Mandanten oder der Systemrolle
	 * @param systemrolleIId wenn null, wird die Mandantenuseranzahl zur&uuml;ckgegeben
	 * @return die aktuelle Useranzahl
	 */
	private List<LpUserCount> getUseranzahl(Integer systemrolleIId) {
		HvTypedQuery<LpUserCount> q = null ;
		if(systemrolleIId == null) {
			q = new HvTypedQuery<LpUserCount>(em.createNamedQuery(LpUserCount.QueryAnzahlMandant)) ;
		} else {
			q = new HvTypedQuery<LpUserCount>(em.createNamedQuery(LpUserCount.QueryAnzahlSystemrolle)) ;
			q.setParameter(2, systemrolleIId);
		}
		q.setMaxResults(1);
		q.setParameter(1, Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));

		return getUseranzahlImpl(q) ;
		
//		// AD
//		// return 0;
//		HvTypedQuery<LpUserCount> q = new HvTypedQuery<LpUserCount>(
//				em.createNamedQuery(systemrolleIId == null ? LpUserCount.QueryAnzahlMandant : LpUserCount.QueryAnzahlSystemrolle));
//		q.setMaxResults(1);
//		q.setParameter(1,
//				Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
//		if(systemrolleIId != null)
//			q.setParameter(2, systemrolleIId);
//		try {
////			Integer anzahl = (Integer) ((List) q.getResultList()).get(0);
////			return anzahl.intValue();
//			/* 
//			 * getResultList() kann auch null liefern
//			 */
//			List<LpUserCount> userList =  q.getResultList() ;
//			if(null == userList) return 0 ;
//			return userList.size() > 0 ? userList.get(0).getIAnzahl() : 0 ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
	}

	private List<LpUserCount> getUseranzahlImpl(HvTypedQuery<LpUserCount> query) {
		List<LpUserCount> defaultUsers = new ArrayList<LpUserCount>() ;
		try {
			List<LpUserCount> users = query.getResultList() ;
			if(users == null) {
				myLogger.warn("Die Liste der angemeldeten Benutzer ist null? Gebe 0 zurueck") ;
				return defaultUsers ;
			}
			
			return users ;
		} catch(Exception e) {
			myLogger.error("Kann die angemeldeten Benutzer nicht ermitteln! Gebe 0 zurueck", e) ;
			return defaultUsers ;
		}
	}
	
	public Integer getIBenutzerFrei(TheClientDto theClientDto)  throws EJBExceptionLP, RemoteException {
		if(theClientDto.getHvmaLizenzId() != null) {
			return Integer.MAX_VALUE;
		} else {
			return getConcurrentUserFrei(theClientDto);
		}
	}
	
	private Integer getConcurrentUserFrei(TheClientDto theClientDto) throws RemoteException {
		UserCountHelper uch = getBenutzerProRolle(theClientDto) ;
		if(uch.getMaxUsers() == -1) {
			uch = getBenutzerProMandant(theClientDto) ;
		}
		
		int frei = uch.getMaxUsers() - uch.getLoggedOnUsersCount() ;
		if(frei < 1) {// && !LPWEBAPPZEMECS.equals(theClientDto.getBenutzername())) {
			myLogger.warn("Die lizenzierte Benutzerzahl wuerde ueberschritten!" +
					" Erlaubt fuer " + uch.getAccount() + 
					", maxUser: " + uch.getMaxUsers() + 
					", derzeit angemeldet: " + uch.getLoggedOnUsersCount() + 
					". Versuch von " + theClientDto.getBenutzername() + " fuer Mandant " + theClientDto.getMandant() + ".") ;

			myLogger.warn(">>>>> Beginn angemeldete Benutzer") ;
			List<TheClientLoggedInDto> users = uch.getUsersAsLoggedIn();
			for (TheClientLoggedInDto theclient : users) {				
				myLogger.warn("Angemeldeter Benutzer: " + theclient.getBenutzername().trim() +
					", #" + theclient.getConcurrentUserCount() + 
					", seit: " + theclient.getDLoggedin() + 
					", Rolle: " + theclient.getSystemrolleCBez() +
						"(Id:"  + theclient.getSystemrolleIId() + ").") ;
			}
			myLogger.warn("<<<<< Ende angemeldete Benutzer") ;
		}
		return frei ;
	}
	
	private UserCountHelper getBenutzerProRolle(
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		SystemrolleDto rolle = getBenutzerFac()
				.systemrolleFindByPrimaryKey(theClientDto.getSystemrolleIId());
		return new UserCountRolleHelperNew(rolle.getIId(), 
				rolle.getIMaxUsers() == null ? -1 : rolle.getIMaxUsers(),
						getTheClientFac().getZaehlerAngemeldeteBenutzerRolle(rolle.getIId()));
//		return new UserCountRolleHelper(rolle.getIId(), 
//				rolle.getIMaxUsers() == null ? -1 : rolle.getIMaxUsers(), getUseranzahl(rolle.getIId())) ;
//		if(rolle.getIMaxUsers() == null) return null;
//		return rolle.getIMaxUsers() - getUseranzahl(rolle.getIId());
	}
	
	private UserCountHelper getBenutzerProMandant(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);

		return new UserCountMandantHelperNew( 
				mandantDto.getIBenutzermax() == null ? 0 : mandantDto.getIBenutzermax(), 
						getTheClientFac().getZaehlerAngemeldeteBenutzer());
		
//		return new UserCountMandantHelper( 
//			mandantDto.getIBenutzermax() == null ? 0 : mandantDto.getIBenutzermax(), 
//					getUseranzahl(null)) ;

//		int userangemeldet = getUseranzahl(null);
//		if (mandantDto.getIBenutzermax() == null)
//			return 0;
//		return mandantDto.getIBenutzermax().intValue() - userangemeldet;
	}

	@Override
	public List<TheClientLoggedInDto> getUsersLoggedInMandant(TheClientDto theClientDto) throws RemoteException {
		UserCountHelper uch = getBenutzerProMandant(theClientDto);
		return uch.getUsersAsLoggedIn();
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
	
	
	@Override
	public TheClientDto logonExtern(int appType, String userName, char[] cKennwortI, Locale uiLocale, String source)
			throws EJBExceptionLP, RemoteException {
		return logonExtern(appType, userName, cKennwortI, uiLocale, null, source) ;
//		Validator.notEmpty(userName, "userName") ;
//		Validator.notNull(cKennwortI, "cKennwortI") ;
//		Validator.notNull(uiLocale, "uiLocale");
//		
//		List<Ansprechpartner> ansprechpartners = AnsprechpartnerQuery.listByEmail(em, userName) ;
//		if(ansprechpartners == null || ansprechpartners.size() == 0) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, userName);			
//		}
//		if(ansprechpartners.size() != 1) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, userName);			
//		}
//		if(!getHashFromExternalPassword(cKennwortI)
//				.equals(ansprechpartners.get(0).getCKennwort())) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IM_KENNWORT, "");			
//		}
//		
//		if(AppType.Stueckliste == appType) {
//			String u = getAppTypeProperty(appType, "user").trim() ;
//			String p = getAppTypeProperty(appType, "pwd").trim() ;
//		
//			List<String> mandanten = findPartlistMandant(userName, ansprechpartners.get(0).getPartnerIId()) ;
//			if(mandanten.size() == 1) {
//				// Als spezifischer HV-Benutzer anmelden
//				TheClientDto theClientDto = logon(
//						u + "|" + source,
//						Helper.getMD5Hash((u + p).toCharArray()), 
//						uiLocale, mandanten.get(0), getTimestamp()) ;
//				
//				// Und die Partner-Id merken fuer spaeter
//				theClientDto.setIStatus(ansprechpartners.get(0).getIId()) ;
//				getTheClientFac().updateTheClient(theClientDto);
//	
//				return theClientDto ;
//			} else {
//				if(mandanten.size() == 0) {
//					throw new EJBExceptionLP(
//							EJBExceptionLP.FEHLER_KEINE_STUECKLISTEN_FUER_PARTNERID, userName) ;					
//				} else {
//					throw new EJBExceptionLP(
//							EJBExceptionLP.FEHLER_KUNDENSTUECKLISTEMANDANT_NICHT_EINDEUTIG, 
//							new ArrayList<Object>(mandanten), new Exception(userName));
//				}
//			}
//		}
//		
//		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG, userName) ;
	}

	@Override
	public TheClientDto logonExtern(int appType, String userName, char[] cKennwortI, Locale uiLocale, String mandantCnr, String source)
			throws EJBExceptionLP, RemoteException {
		Validator.notEmpty(userName, "userName") ;
		Validator.notNull(cKennwortI, "cKennwortI") ;
		Validator.notNull(uiLocale, "uiLocale");
		
		List<Ansprechpartner> ansprechpartners = AnsprechpartnerQuery.listByEmail(em, userName) ;
		if(ansprechpartners == null || ansprechpartners.size() == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, userName);			
		}
		if(ansprechpartners.size() != 1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, userName);			
		}
		if(!getHashFromExternalPassword(cKennwortI)
				.equals(ansprechpartners.get(0).getCKennwort())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IM_KENNWORT, "");			
		}
		
		if(AppType.Stueckliste == appType) {
			String u = getAppTypeProperty(appType, "user").trim() ;
			String p = getAppTypeProperty(appType, "pwd").trim() ;

			if(Helper.isStringEmpty(mandantCnr)) {
				List<String> mandanten = findPartlistMandant(userName, ansprechpartners.get(0).getPartnerIId()) ;
				if(mandanten.size() == 1) {
					mandantCnr = mandanten.get(0) ;
				} else {
					if(mandanten.size() == 0) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_KEINE_STUECKLISTEN_FUER_PARTNERID, userName) ;
					}
					
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_KUNDENSTUECKLISTEMANDANT_NICHT_EINDEUTIG, 
							new ArrayList<Object>(mandanten), new Exception(userName));
				}
			}
			
			// Als spezifischer HV-Benutzer anmelden
			TheClientDto theClientDto = logon(
					u + "|" + source,
					Helper.getMD5Hash((u + p).toCharArray()), 
					uiLocale, mandantCnr, getTimestamp()) ;
			
			// Und die Partner-Id merken fuer spaeter
			theClientDto.setIStatus(ansprechpartners.get(0).getIId()) ;
			getTheClientFac().updateTheClient(theClientDto);

			return theClientDto ;				
		}
		
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG, userName) ;
	}
	
	private List<String> findPartlistMandant(String userName, Integer partnerId) {
		List<String> mandanten = new ArrayList<String>() ;
		List<Kunde> kunden = KundeQuery.listByPartnerId(em, partnerId) ;
		if(kunden.size() == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, userName);	
		}

		for (Kunde kunde : kunden) {
			String mandant = kunde.getMandantCNr() ;
		
			HvTypedQuery<Long> q = new HvTypedQuery<Long>(
					em.createNamedQuery("StuecklistefindCountByPartnerIIdMandantCNr")) ;
			q.setParameter(1, partnerId) ;
			q.setParameter(2, mandant) ;
			
			Long count = q.getSingleResult() ;
			if(count != null && count > 0) {
				mandanten.add(mandant) ;				
			}				
//			if(stuecklisten.size() > 0) {
//				mandanten.add(mandant) ;
//			}
		}
		
		return mandanten ;
	}
	
	private String getAppTypeProperty(int appType, String token) {
		return ServerConfiguration.getAppTypeProperty(appType, token) ;
	}
	
	private String getHashFromExternalPassword(char[] password) {
		return new String(Helper.getMD5Hash(password)) ;		
	}
	
	public TheClientDto logonIntern(Locale locUII, String sMandantI){
		return logonImpl(Helper.getFullUsername(LPWEBAPPZEMECS), 
				Helper.getMD5Hash((LPWEBAPPZEMECS +LPWEBAPPZEMECS).toCharArray()), 
				locUII, sMandantI, getTimestamp(), true, null, null);
	}
	
	private class SystemrolleCache extends HvCreatingCachingProvider<Integer, String> {
		@Override
		protected String provideValue(Integer key, Integer transformedKey) {
			Systemrolle r = em.find(Systemrolle.class, transformedKey);
			return r == null ? "" : r.getCBez();				
		}
	}
	
	private abstract class UserCountHelper {
		private int maxUsers;
		private List<LpUserCount> loggedInUsers;
		private Integer rolleId;
		private String account;
		
		public UserCountHelper(int maxUsers, List<LpUserCount> users) {
			this.maxUsers = maxUsers;
			this.loggedInUsers = users;
		}
		
		public int getMaxUsers() {
			return maxUsers;
		}
		
		public List<LpUserCount> getLoggedOnUsers() {
			return loggedInUsers;
		}
		
		public int getLoggedOnUsersCount() {
			return loggedInUsers.size() > 0 ? loggedInUsers.get(0).getIAnzahl() : 0;
		}

		public Integer getRolleId() {
			return rolleId;
		}

		public void setRolleId(Integer rolleId) {
			this.rolleId = rolleId;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}
		
		public abstract List<Theclient> getUsers();
		
		public List<TheClientLoggedInDto> getUsersAsLoggedIn() throws RemoteException {
			List<TheClientLoggedInDto> clients = new ArrayList<TheClientLoggedInDto>();
			String lastUser = "";
			int userCount = 0;
			SystemrolleCache cache = new SystemrolleCache();
			for (Theclient emClient : getUsers()) {
				TheClientDto dto = getTheClientFac().theClientFindByPrimaryKey(emClient.getCNr());
				if(!lastUser.equals(dto.getBenutzername())) {
					++userCount;
					lastUser = dto.getBenutzername();
				}
				TheClientLoggedInDto inDto = dto.asLoggedIn();
				inDto.setConcurrentUserCount(userCount);
				inDto.setSystemrolleCBez(cache.getValueOfKey(inDto.getSystemrolleIId()));
				clients.add(inDto);
			}
			return clients;
			
		}
	}
	
	private class UserCountMandantHelper extends UserCountHelper {
		public UserCountMandantHelper(int maxUsers, List<LpUserCount> users) {
			super(maxUsers, users);
			setAccount("Mandant");
		}
		
		@Override
		public List<Theclient> getUsers() {
			return new ArrayList<Theclient>();
		}
	}

	private class UserCountMandantHelperNew extends UserCountHelper {
		private final int loggedInUsers;
		
		public UserCountMandantHelperNew(int maxUsers, int loggedInUsers) {
			super(maxUsers, new ArrayList<LpUserCount>());
			setAccount("Mandant");
			this.loggedInUsers = loggedInUsers;
		}
		
		public int getLoggedOnUsersCount() {
			return loggedInUsers;
		}
		
		@Override
		public List<Theclient> getUsers() {
			HvTypedQuery<Theclient> q = new HvTypedQuery<Theclient>(
					em.createNamedQuery("TheClientsMandantLoggedIn")
						.setParameter("today", Helper.cutTimestamp(getTimestamp())));
			return q.getResultList();
		}
	}
	
	private class UserCountRolleHelper extends UserCountHelper {
		public UserCountRolleHelper(Integer rolleId, int maxUsers, List<LpUserCount> users) {
			super(maxUsers, users);
			setAccount("Rolle: " + rolleId);
			setRolleId(rolleId);
		}		
		@Override
		public List<Theclient> getUsers() {
			return new ArrayList<Theclient>();
		}
	}
	
	private class UserCountRolleHelperNew extends UserCountHelper {
		private final int loggedInUsers;
		
		public UserCountRolleHelperNew(Integer rolleId, int maxUsers, int loggedInUsers) {
			super(maxUsers, new ArrayList<LpUserCount>());
			setAccount("Rolle: " + rolleId);
			setRolleId(rolleId);
			this.loggedInUsers = loggedInUsers;
		}

		public int getLoggedOnUsersCount() {
			return loggedInUsers;
		}
		
		@Override
		public List<Theclient> getUsers() {
			HvTypedQuery<Theclient> q = new HvTypedQuery<Theclient>(
					em.createNamedQuery("TheClientsRolleLoggedIn")
						.setParameter("today", Helper.cutTimestamp(getTimestamp()))
						.setParameter("rolle", getRolleId()));
			return q.getResultList();
		}
	}
}
