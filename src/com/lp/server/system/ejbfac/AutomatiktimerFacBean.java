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
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.Collection;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.automatikjob.AutomatikjobBasis;
import com.lp.server.system.automatikjob.AutomatikjobBestellvorschlagberechnung;
import com.lp.server.system.automatikjob.AutomatikjobBestellvorschlagdruck;
import com.lp.server.system.automatikjob.AutomatikjobBestellvorschlagverdichtung;
import com.lp.server.system.automatikjob.AutomatikjobFehlmengendruck;
import com.lp.server.system.automatikjob.AutomatikjobKassenimport;
import com.lp.server.system.automatikjob.AutomatikjobMahnen;
import com.lp.server.system.automatikjob.AutomatikjobMahnungsversand;
import com.lp.server.system.automatikjob.AutomatikjobPaternoster;
import com.lp.server.system.automatikjob.AutomatikjobRahmenbedarfePruefen;
import com.lp.server.system.automatikjob.AutomatikjobRahmendetailbedarfdruck;
import com.lp.server.system.automatikjob.AutomatikjobSofortverbrauch;
import com.lp.server.system.ejb.Automatiktimer;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobFac;
import com.lp.server.system.service.AutomatikjobtypeDto;
import com.lp.server.system.service.AutomatikjobtypeFac;
import com.lp.server.system.service.AutomatiktimerDto;
import com.lp.server.system.service.AutomatiktimerDtoAssembler;
import com.lp.server.system.service.AutomatiktimerFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.Facade;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AutomatiktimerFacBean extends Facade implements AutomatiktimerFac{

	@Resource javax.ejb.TimerService timerService;

	@PersistenceContext
	private EntityManager em;

	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			AutomatikjobBasis.class);

	private LogonFac logonFac;
	private TheClientFac theClientFac;
	private PersonalFac personalFac;
	private VersandFac versandFac;
	private AutomatikjobDto automatikjobDto;
	private AutomatikjobFac automatikjobFac;
	private AutomatikjobtypeFac automatikjobtypeFac;

	private TheClientDto theClientDto;



	protected final static String sUsername = "lpwebappzemecs";
	protected final static String sUser = sUsername + "|Automatik|";
	protected final static char[] cPasswd = new char[] {
		'l', 'p', 'w', 'e', 'b', 'a', 'p', 'p', 'z', 'e', 'm', 'e', 'c', 's'};

	@Timeout
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void ejbTimeout(javax.ejb.Timer timer) {
		myLogger.info("Automatik start");
		//prepare next Timeout in 1 day
		timer.cancel();
		boolean bPerform = true;
		Date tTimeStarted = getDate();
		long loneHourInMillis = 60 * 60 * 1000;
		long loneDayInMillis = loneHourInMillis * 24;
		try {
			setTimer(loneDayInMillis);
		} catch (RemoteException e) {
			myLogger.error(e.getMessage());
		}
		boolean bDayIsNonWorkingDay = false;
		boolean bLastDayOfMonth = checkLastDayOfMonth();
		//Check if Automatik is activated
		Integer iEnabled = 0;
		try {
			iEnabled = automatiktimerFindByPrimaryKey(0).getBEnabled();
		}
		catch (Throwable t) {
			myLogger.error("Die Konfiguration f\u00FCr den Timer konnte nicht gefunden werden");
		}
		if (iEnabled != 0) {
			// jobtype pasternoster
			Integer jobtypPasternoster = null;
			AutomatikjobtypeDto jobtypDto = null;
			try {
				jobtypDto = getAutomatikjobtypeFac().automatikjobtypeFindByCJobType(
						AutomatiktimerFac.JOBTYPE_PATERNOSTERABFRAGE_TYPE);
				jobtypPasternoster = jobtypDto.getIId();
			} catch (Throwable t) {
				//
			}
			
			//fetch Jobs like sorted
			int iActJob = 0;

			try {
				theClientFac = getTheClientFac();
				automatikjobFac = getAutomatikjobFac();
				automatikjobtypeFac = getAutomatikjobtypeFac();
				theClientFac = getTheClientFac();
				logonFac = getLogonFac();
			}
			catch (Throwable t) {
				myLogger.error("Fehler bei der Initialisierung der Daten");
			}
			try {
				automatikjobDto = automatikjobFac.automatikjobFindByISort(iActJob);
			}
			catch (Throwable t) {
				myLogger.error("Es konnten keine automatischen Jobs gefunden werden");
			}
			//Login
			try {
				theClientDto = logonFac.logon(sUser, 
						Helper.getMD5Hash((sUsername + new String(cPasswd)).toCharArray()), 
						Helper.string2Locale("deAT      "), null,
						getTimestamp());
			}
			catch (Throwable t) {
				myLogger.error("Der verwendete Benutzer konnte sich nicht anmelden");
			}
			
			Automatiktimer autoTimer = null;
			Time performHour;
			try {
				autoTimer = em.find(Automatiktimer.class, new Integer(0));
			} catch (Exception e) {
				myLogger.error("Automatiktimer ID=0 konnte nicht gefunden werden");
			}
			if (autoTimer == null)
				// nicht gesetzt, aktuellen Zeitpunkt nehmen 
				performHour = new Time(System.currentTimeMillis());
			else
				// sonst Zeit aus Bean
				performHour = autoTimer.getTTimetoperform();

			String MandantCNr = null;
			try {
				MandantCNr = theClientDto.getMandant();
				bDayIsNonWorkingDay = checkNonWorkingDay(performHour, theClientDto);
			}
			catch (Throwable t) {
				myLogger.error("Der Standardmandant konnte nicht gefunden werden");
			}
			try {
				logonFac.logout(theClientDto);
			} catch (Throwable t) {
				myLogger.error("Fehler beim abmelden des Benutzers");
			}
			
			while (automatikjobDto != null) {
				if (!automatikjobDto.getCMandantCNr().equals(MandantCNr)) {
					//Change Mandant if necessary
					MandantCNr = automatikjobDto.getCMandantCNr();
					myLogger.info(MandantCNr);
					try {
						theClientDto = logonFac.logon(sUser,
								Helper.getMD5Hash((sUsername + new String(cPasswd)).toCharArray()), 
								Helper.string2Locale("deAT      "),
								MandantCNr, theClientDto,
								getTimestamp());
					}
					catch (Throwable t) {
						myLogger.error("Das wechseln des Mandants schlug fehl");
					}
					try {
						MandantCNr = theClientDto.getMandant();
						bDayIsNonWorkingDay = checkNonWorkingDay(performHour, theClientDto);
					}
					catch (Throwable t) {
						myLogger.error("Der Mandant wurde nicht gefunden");
					}
				}
				//Abfrage ob der Job aktiviert ist
				if (automatikjobDto.getBActive() == 0) {
					bPerform = false;
				}
				else {
					//Abfrage ob der Intervall bis zur naechsten ausfuehrung stimmt
					if(automatikjobDto.getDLastperformed() == null){
						//Job wurde noch nie durchgefuehrt...
						automatikjobDto.setDLastperformed(new Date(0));
					}
					long intervalTime = loneDayInMillis * automatikjobDto.getIIntervall();
					long lLastperform = automatikjobDto.getDLastperformed().getTime();
					long lLastPlusIntervall = lLastperform + intervalTime;
					Date dLastPlusIntervall = new Date(lLastPlusIntervall);
					if (! (dLastPlusIntervall.equals(getDate()) ||
							dLastPlusIntervall.before(getDate()))) {
						bPerform = false;
					}
					else {
						//Wenn der Intervall stimmt jedoch ein Feiertag folgt
						if (bDayIsNonWorkingDay) {
							if (automatikjobDto.getBPerformOnNonWOrkingDays() == 0) {
								bPerform = false;
							}
						}
					}
					//Wenn der Monatsletzte ist und es ein Monatsjob ist fuehre diesen aus
					if (bLastDayOfMonth) {
						if (automatikjobDto.getBMonthjob() != 0) {
							bPerform = true;
						}
					}
				}
				
				if ((jobtypPasternoster != null) && (automatikjobDto.getIAutomatikjobtypeIid().equals(jobtypPasternoster))) {
					// Paternosterjobs nicht ausfuehren, werden von AutoPaternosterFacBean durchgefuehrt
					bPerform = false;
				}
				
				//Wenn der Job auszufuehren ist
				if (bPerform) {
					AutomatikjobBasis automatikjobBasis = getClassForJobType(automatikjobDto.
							getIAutomatikjobtypeIid());
					//ausfuehren des aktuellen Jobs
					if (automatikjobBasis.performJob(theClientDto)) {
						sendMailToUser();
					} else {
						automatikjobDto.setDLastperformed(tTimeStarted);
						automatikjobDto.setDNextperform(new Date(automatikjobDto.
								getDLastperformed().getTime() +
								(automatikjobDto.getIIntervall() *
										loneDayInMillis)));
					}
					try {
						automatikjobFac.updateAutomatikjob(automatikjobDto);
					}
					catch (RemoteException ex1) {
						myLogger.error("Fehler beim beenden des Jobs");
					}
				}
				bPerform = true;
				iActJob++;
				try {
					automatikjobDto = automatikjobFac.automatikjobFindByISort(iActJob);
				}
				catch (Exception oNFEx) {
					automatikjobDto = null;
				}
				try {
					logonFac.logout(theClientDto);
				}
				catch (Throwable t) {
					myLogger.error("Fehler beim abmelden des Benutzers");
				}

			}
		}
		myLogger.info("Automatik ende");
	}

	public AutomatiktimerDto automatiktimerFindByPrimaryKey(Integer id)
	throws RemoteException {
		Automatiktimer automatiktimer = em.find(Automatiktimer.class, id);
		if(automatiktimer==null){
			return null;
		}
		return assembleAutomatiktimerDto(automatiktimer);
	}

	public void createAutomatiktimer(AutomatiktimerDto automatiktimerDto)
	throws RemoteException {
		Automatiktimer automatiktimer = em.find(Automatiktimer.class,automatiktimerDto.getIId());
		automatiktimer = setAutomatiktimerFromAutomatiktimerDto(automatiktimer, automatiktimerDto);
		em.persist(automatiktimer);
		em.flush();

	}

	public void removeAutomatiktimer(Integer id) throws RemoteException {
		Automatiktimer toRemove = em.find(Automatiktimer.class, id);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAutomatiktimer(AutomatiktimerDto automatiktimerDto)
	throws RemoteException {
		removeAutomatiktimer(automatiktimerDto.getIId());

	}


	public void setTimer(long millisTillStart) throws RemoteException {
		Collection<?> timers = timerService.getTimers();
		Object[] actTimers = timers.toArray();
		for (int i = 0; i < actTimers.length; i++) {
			Timer timer = (Timer) actTimers[i];
			timer.cancel();
		}
		timerService.createTimer(millisTillStart, null); ;

	}


	public void updateAutomatiktimer(AutomatiktimerDto automatiktimerDto)
	throws RemoteException {
		Automatiktimer automatiktimer = em.find(Automatiktimer.class,automatiktimerDto.getIId());
		automatiktimer = setAutomatiktimerFromAutomatiktimerDto(automatiktimer, automatiktimerDto);
		em.persist(automatiktimer);
		em.flush();

	}


	public void updateAutomatiktimers(AutomatiktimerDto[] automatiktimerDtos)
	throws RemoteException {
		for(int i=0;i<automatiktimerDtos.length;i++){
			Automatiktimer automatiktimer = em.find(Automatiktimer.class,automatiktimerDtos[i].getIId());
			automatiktimer = setAutomatiktimerFromAutomatiktimerDto(automatiktimer, automatiktimerDtos[i]);
			em.persist(automatiktimer);
			em.flush();
		}

	}

	private boolean checkNonWorkingDay(Time performHour, TheClientDto theClientDto) {
		/*
		 * PJ 16246
		 * 
		 * nicht ausf&uuml;hren (ausf&uuml;hren)
		 * Start vor Mittag < 12: 	Sa So (Mo Di Mi Do Fr)
		 * Start nach Mittag >=12:	Fr Sa (So Mo Di Mi Do)
		 */
		Calendar cal = Calendar.getInstance();
		cal.setTime(performHour);
		if (cal.get(Calendar.HOUR_OF_DAY) < 12) {
			cal = Calendar.getInstance();
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			}
		} else {
			cal = Calendar.getInstance();
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
					|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				return true;
			}
		}

		try {
			personalFac = getPersonalFac();
			BetriebskalenderDto calDto = personalFac.betriebskalenderFindByMandantCNrDDatum(
					getTimestamp(), theClientDto.getMandant(), theClientDto);
			//TagesartIId 18 = Feiertag
			if (calDto.getTagesartIId() == 18) {
				return true;
			}
		}
		catch (Throwable t) {
			return false;
		}
		return false;
	}
/*	
	@Deprecated
	private boolean checkNonWorkingDay(TheClientDto theClientDto) {
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
				|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return true;
		}

		try {
			personalFac = getPersonalFac();
			BetriebskalenderDto calDto = personalFac.betriebskalenderFindByMandantCNrDDatum(
					getTimestamp(), theClientDto.getMandant(), theClientDto);
			//TagesartIId 18 = Feiertag
			if (calDto.getTagesartIId() == 18) {
				return true;
			}
		}
		catch (Throwable t) {
			return false;
		}
		return false;
	}
*/
	
	private boolean checkLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_MONTH) == cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			return true;
		}
		return false;
	}

	private void sendMailToUser() {
		try {
			versandFac = getVersandFac();
			VersandauftragDto versandauftragDto = new VersandauftragDto();
			versandauftragDto.setOInhalt(null);
			versandauftragDto.setBEmpfangsbestaetigung(new Short("0"));
			String mailtext = "Dieses E-mail benachrichtigt Sie dar\u00FCber, dass ein Fehler w\u00E4hrend der Ausf\u00FChrung der automatischen Jobs aufgetreten ist. Bitte \u00FCberpr\u00FCfen Sie das Logfile ihres Helium V Servers.";
			versandauftragDto.setCText(mailtext);
			PersonalDto persDto = personalFac.personalFindByPrimaryKey(
					theClientDto.getIDPersonal(), theClientDto);
			String sAbsender = persDto.getPartnerDto().getCEmail();
			versandauftragDto.setCAbsenderadresse(sAbsender);
			//Empfaenger = Absender da der ZUstaendige User selbst benachrichtigt wird
			versandauftragDto.setCEmpfaenger(sAbsender);
			String sBetreff = "Automatische Jobs Helium V -  Es ist ein Fehler aufgetreten.";
			versandauftragDto.setCBetreff(sBetreff);
			versandFac.createVersandauftrag(versandauftragDto,false,theClientDto);
		}
		catch (Throwable t) {
			myLogger.error("Fehler beim versenden der Fehlerbenachrichtigung");
		}

	}


	private AutomatikjobBasis getClassForJobType(int iJobtype) {
		try {
			AutomatikjobtypeDto automatikjobtypeDto = automatikjobtypeFac.
			automatikjobtypeFindByPrimaryKey(iJobtype);
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_NO_TYPE)) {
				return null;
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_FEHLMENGENDRUCK_TYPE)) {
				return new AutomatikjobFehlmengendruck();
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_MAHNEN_TYPE)) {
				return new AutomatikjobMahnen();
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_MAHNUNGSVERSAND_TYPE)) {
				return new AutomatikjobMahnungsversand();
			}
			if (automatikjobtypeDto.getCJobtype().equals(
					JOBTYPE_BESTELLVORSCHLAGBERECHNUNG_TYPE)) {
				return new AutomatikjobBestellvorschlagberechnung();
			}
			if (automatikjobtypeDto.getCJobtype().equals(
					JOBTYPE_BESTELLVORSCHLAGVERDICHTUNG_TYPE)) {
				return new AutomatikjobBestellvorschlagverdichtung();
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_BESTELLVORSCHLAGDRUCK_TYPE)) {
				return new AutomatikjobBestellvorschlagdruck();
			}
			if(automatikjobtypeDto.getCJobtype().equals(JOBTYPE_RAHMENBEDARFEPRUEFEN_TYPE)){
				return new AutomatikjobRahmenbedarfePruefen();
			}
			if(automatikjobtypeDto.getCJobtype().equals(JOBTYPE_RAHMENDETAILBEDARFDRUCK_TYPE)){
				return new AutomatikjobRahmendetailbedarfdruck();
			}
			if(automatikjobtypeDto.getCJobtype().equals(JOBTYPE_PATERNOSTERABFRAGE_TYPE)){
				return new AutomatikjobPaternoster();
			}
			if(automatikjobtypeDto.getCJobtype().equals(JOBTYPE_SOFORTVERBRAUCH_TYPE)){
				return new AutomatikjobSofortverbrauch();
			}
			
			if(automatikjobtypeDto.getCJobtype().equals(JOBTYPE_IMPORTKASSENFILES_TYPE)){
				return new AutomatikjobKassenimport();
			}
		}
		catch (RemoteException ex) {
		}

		return null;
	}

	private Automatiktimer setAutomatiktimerFromAutomatiktimerDto(Automatiktimer automatiktimer,
			AutomatiktimerDto automatiktimerDto) {
		automatiktimer.setBEnabled(automatiktimerDto.getBEnabled());
		automatiktimer.setTTimetoperform(automatiktimerDto.getTTimetoperform());
		return automatiktimer;
	}
	private AutomatiktimerDto assembleAutomatiktimerDto(Automatiktimer automatiktimer) {
		return AutomatiktimerDtoAssembler.createDto(automatiktimer);
	}
}
