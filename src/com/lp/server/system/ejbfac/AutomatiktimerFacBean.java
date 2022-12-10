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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.personal.service.BetriebskalenderDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.automatikjob.Automatikjob4VendingExport;
import com.lp.server.system.automatikjob.AutomatikjobArbeitszeitstatusPDFDruck;
import com.lp.server.system.automatikjob.AutomatikjobAuslieferlistePDFDruck;
import com.lp.server.system.automatikjob.AutomatikjobBasis;
import com.lp.server.system.automatikjob.AutomatikjobBedarfsuebernahmeOffene;
import com.lp.server.system.automatikjob.AutomatikjobBestellvorschlagberechnung;
import com.lp.server.system.automatikjob.AutomatikjobBestellvorschlagdruck;
import com.lp.server.system.automatikjob.AutomatikjobBestellvorschlagverdichtung;
import com.lp.server.system.automatikjob.AutomatikjobFehlmengendruck;
import com.lp.server.system.automatikjob.AutomatikjobInternebestellungberechnen;
import com.lp.server.system.automatikjob.AutomatikjobKassenimport;
import com.lp.server.system.automatikjob.AutomatikjobKpiPdfDruck;
import com.lp.server.system.automatikjob.AutomatikjobLoseerledigen;
import com.lp.server.system.automatikjob.AutomatikjobLumiquote;
import com.lp.server.system.automatikjob.AutomatikjobMahnen;
import com.lp.server.system.automatikjob.AutomatikjobMahnungsversand;
import com.lp.server.system.automatikjob.AutomatikjobMailAblageIMAP;
import com.lp.server.system.automatikjob.AutomatikjobMailversand;
import com.lp.server.system.automatikjob.AutomatikjobMonatsabrechnungVersand;
import com.lp.server.system.automatikjob.AutomatikjobMonatsabrechnungVersandAbteilungen;
import com.lp.server.system.automatikjob.AutomatikjobNachtraeglichGeoeffneteLosErledigen;
import com.lp.server.system.automatikjob.AutomatikjobPaternoster;
import com.lp.server.system.automatikjob.AutomatikjobRahmenbedarfePruefen;
import com.lp.server.system.automatikjob.AutomatikjobRahmendetailbedarfdruck;
import com.lp.server.system.automatikjob.AutomatikjobSofortverbrauch;
import com.lp.server.system.automatikjob.AutomatikjobWEJournal;
import com.lp.server.system.automatikjob.AutomatikjobWebabfrageArtikellieferant;
import com.lp.server.system.ejb.Automatikjobs;
import com.lp.server.system.ejb.AutomatikjobsQuery;
import com.lp.server.system.ejb.Automatiktimer;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobDtoAssembler;
import com.lp.server.system.service.AutomatikjobtypeDto;
import com.lp.server.system.service.AutomatiktimerDto;
import com.lp.server.system.service.AutomatiktimerDtoAssembler;
import com.lp.server.system.service.AutomatiktimerFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.Facade;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AutomatiktimerFacBean extends Facade implements AutomatiktimerFac{
	private static final long loneHourInMillis = 60 * 60 * 1000;
	private static final long loneDayInMillis = loneHourInMillis * 24;

	@Resource 
	javax.ejb.TimerService timerService;

	@PersistenceContext
	private EntityManager em;

	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			AutomatikjobBasis.class);

//	private AutomatikjobDto automatikjobDto;
	private TheClientDto theClientDto;
	private String lastMandantCnr;

	@Timeout
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void ejbTimeout(javax.ejb.Timer timer) {
		myLogger.info("Automatik start");
		//prepare next Timeout in 1 day
		timer.cancel();
		try {
			setTimer(loneDayInMillis);
		} catch (RemoteException e) {
			myLogger.error(e.getMessage());
		}
		//Check if Automatik is activated
		Integer iEnabled = 0;
		try {
			iEnabled = automatiktimerFindByPrimaryKey(0).getBEnabled();
		}
		catch (Throwable t) {
			myLogger.error("Die Konfiguration f\u00FCr den Timer konnte nicht gefunden werden");
		}
		if (iEnabled != 0) {
			processJobs();
		}
		myLogger.info("Automatik ende");
	}

	private boolean changeToMandant(String newMandantCnr) {
		if (newMandantCnr.equals(lastMandantCnr)) {
			return true;
		}
		
		try {
			theClientDto = getLogonFac().logonIntern(
				getMandantFac().getLocaleDesHauptmandanten(), newMandantCnr);
			lastMandantCnr = theClientDto.getMandant();
			return true;
		} catch (Throwable t) {
			myLogger.error("Der interne Logon in Mandant " + newMandantCnr + " schlug fehl.", t);
			return false;
		}
	}
	
	private void processJobsOld() {
		Timestamp tTimeStarted = getTimestamp();
		boolean bDayIsNonWorkingDay = false;
		boolean bLastDayOfMonth = checkLastDayOfMonth();
		//Login
		try {
			theClientDto = getLogonFac().logonIntern(getMandantFac().getLocaleDesHauptmandanten(), null);
		} catch (Throwable t) {
			myLogger.error("Der verwendete Benutzer konnte sich nicht anmelden", t);
		}
		
		Automatiktimer autoTimer = null;
		Time performHour;
		try {
			autoTimer = em.find(Automatiktimer.class, new Integer(0));
		} catch (Exception e) {
			myLogger.error("Automatiktimer ID=0 konnte nicht gefunden werden", e);
		}
		if (autoTimer == null)
			// nicht gesetzt, aktuellen Zeitpunkt nehmen 
			performHour = new Time(System.currentTimeMillis());
		else
			// sonst Zeit aus Bean
			performHour = autoTimer.getTTimetoperform();

		String mandantCNr = null;
		try {
			mandantCNr = theClientDto.getMandant();
			bDayIsNonWorkingDay = checkNonWorkingDay(performHour, theClientDto);
		} catch (Throwable t) {
			myLogger.error("Der Standardmandant konnte nicht gefunden werden", t);
		}

		try {
			getLogonFac().logout(theClientDto);
		} catch (Throwable t) {
			myLogger.error("Fehler beim Abmelden des Benutzers", t);
		}

		AutomatikjobDto[] jobs = listActiveJobs();
		for (AutomatikjobDto automatikjobDto : jobs) {
			boolean bPerform = true;
			if (!automatikjobDto.getCMandantCNr().equals(mandantCNr)) {
				//Change Mandant if necessary
				mandantCNr = automatikjobDto.getCMandantCNr();
				myLogger.info(mandantCNr);
				try {
					theClientDto = getLogonFac().logonIntern(
						getMandantFac().getLocaleDesHauptmandanten(), mandantCNr);
				} catch (Throwable t) {
					myLogger.error("Das wechseln des Mandants schlug fehl", t);
				}
				try {
					mandantCNr = theClientDto.getMandant();
					bDayIsNonWorkingDay = checkNonWorkingDay(performHour, theClientDto);
				} catch (Throwable t) {
					myLogger.error("Der Mandant wurde nicht gefunden", t);
				}
			}
			//Abfrage ob der Intervall bis zur naechsten ausfuehrung stimmt
			if(automatikjobDto.getDLastperformed() == null){
				//Job wurde noch nie durchgefuehrt...
				automatikjobDto.setDLastperformed(new Timestamp(0));
			}
			long intervalTime = loneDayInMillis * automatikjobDto.getIIntervall();
			long lLastperform = automatikjobDto.getDLastperformed().getTime();
			long lLastPlusIntervall = lLastperform + intervalTime;
			Date dLastPlusIntervall = new Date(lLastPlusIntervall);
			if (! (dLastPlusIntervall.equals(getDate()) ||
					dLastPlusIntervall.before(getDate()))) {
				bPerform = false;
			} else {
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
			
			//Wenn der Job auszufuehren ist
			if (bPerform) {
				AutomatikjobBasis automatikjobBasis = getClassForJobType(automatikjobDto.
						getIAutomatikjobtypeIid());
				//ausfuehren des aktuellen Jobs
				if (automatikjobBasis.performJob(theClientDto)) {
					sendMailToUser();
				} else {
					automatikjobDto.setDLastperformed(Helper.cutTimestamp(tTimeStarted));
					automatikjobDto.setDNextperform(Helper.cutTimestamp(new Timestamp(automatikjobDto.
							getDLastperformed().getTime() +
							(automatikjobDto.getIIntervall() *
									loneDayInMillis))));
				}
				try {
					getAutomatikjobFac().updateAutomatikjob(automatikjobDto);
				} catch (RemoteException ex1) {
					myLogger.error("Fehler beim Beenden des Jobs", ex1);
				}
			}
				
			try {
				getLogonFac().logout(theClientDto);
			} catch (Throwable t) {
				myLogger.error("Fehler beim Abmelden des Benutzers", t);
			}			
		}		
	}
	

	/** 
	 * Alle aktivierten tageweisen Jobs (Standard Automatikjob)</br>
	 * <p>Hier erfolgt bewusst die Umwandlung der Entities in Dtos, mit
	 * der Hoffnung, dass die Entities moeglichst rasch aus dem 
	 * Transaktionsmanager entfernt werden. Die Jobs koennen durchaus 
	 * auch laenger laufen.
	 * 
	 * @return eine Liste aller aktivierten Standard-Jobs nach ISORT 
	 */
	private AutomatikjobDto[] listActiveJobs() {
		List<Automatikjobs> jobs = AutomatikjobsQuery
				.listByActiveScheduler(em, true, Scheduler.AUTOMATIKJOB);
		return AutomatikjobDtoAssembler.createDtos(jobs);
	}
	
	public AutomatiktimerDto automatiktimerFindByPrimaryKey(
			Integer id) throws RemoteException {
		Automatiktimer automatiktimer = em.find(Automatiktimer.class, id);
		if(automatiktimer==null){
			return null;
		}
		return assembleAutomatiktimerDto(automatiktimer);
	}

	public void createAutomatiktimer(
			AutomatiktimerDto automatiktimerDto) throws RemoteException {
		Automatiktimer automatiktimer = em.find(
				Automatiktimer.class,automatiktimerDto.getIId());
		automatiktimer = setAutomatiktimerFromAutomatiktimerDto(
				automatiktimer, automatiktimerDto);
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
		Collection<Timer> timers = timerService.getTimers();
		for (Timer timer : timers) {
			timer.cancel();
		}
		timerService.createTimer(millisTillStart, null);
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
			BetriebskalenderDto calDto = getPersonalFac()
				.betriebskalenderFindByMandantCNrDDatum(
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
	
	private boolean checkLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_MONTH) == cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			return true;
		}
		return false;
	}

	private void sendMailToUser() {
		try {
			VersandauftragDto versandauftragDto = new VersandauftragDto();
			versandauftragDto.setOInhalt(null);
			versandauftragDto.setBEmpfangsbestaetigung(new Short("0"));
			String mailtext = "Dieses E-mail benachrichtigt Sie dar\u00FCber, dass ein Fehler w\u00E4hrend der Ausf\u00FChrung der automatischen Jobs aufgetreten ist. Bitte \u00FCberpr\u00FCfen Sie das Logfile ihres Helium V Servers.";
			versandauftragDto.setCText(mailtext);
			PersonalDto persDto = getPersonalFac().personalFindByPrimaryKey(
					theClientDto.getIDPersonal(), theClientDto);
			String sAbsender = persDto.getPartnerDto().getCEmail();
			versandauftragDto.setCAbsenderadresse(sAbsender);
			//Empfaenger = Absender da der ZUstaendige User selbst benachrichtigt wird
			versandauftragDto.setCEmpfaenger(sAbsender);
			String sBetreff = "Automatische Jobs Helium V -  Es ist ein Fehler aufgetreten.";
			versandauftragDto.setCBetreff(sBetreff);
			getVersandFac().createVersandauftrag(versandauftragDto,false,theClientDto);
		}
		catch (Throwable t) {
			myLogger.error("Fehler beim Versenden der Fehlerbenachrichtigung", t);
		}
	}


	private AutomatikjobBasis getClassForJobType(int iJobtype) {
		try {
			AutomatikjobtypeDto automatikjobtypeDto = 
					getAutomatikjobtypeFac()
						.automatikjobtypeFindByPrimaryKey(iJobtype);
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_NO_TYPE)) {
				return null;
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_FEHLMENGENDRUCK_TYPE)) {
				return new AutomatikjobFehlmengendruck();
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_MAHNEN_TYPE)) {
				return new AutomatikjobMahnen();
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_LOSEERLEDIGEN)) {
				return new AutomatikjobLoseerledigen();
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_MAHNUNGSVERSAND_TYPE)) {
				return new AutomatikjobMahnungsversand();
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_MONATSABRECHNUNGVERSAND_TYPE)) {
				return new AutomatikjobMonatsabrechnungVersand();
			}
			if (automatikjobtypeDto.getCJobtype().equals(JOBTYPE_MONATSABRECHNUNGVERSAND_ABTEILUNGEN_TYPE)) {
				return new AutomatikjobMonatsabrechnungVersandAbteilungen();
			}
			
			if (automatikjobtypeDto.getCJobtype().equals(
					JOBTYPE_BESTELLVORSCHLAGBERECHNUNG_TYPE)) {
				return new AutomatikjobBestellvorschlagberechnung();
			}
			if (automatikjobtypeDto.getCJobtype().equals(
					JOBTYPE_INTERNEBESTELLUNGBERECHNUNG_TYPE)) {
				return new AutomatikjobInternebestellungberechnen();
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
			if (JOBTYPE_LUMIQUOTE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobLumiquote();
			}
			
			if (JOBTYPE_AUSLIEFERLISTEPDFDRUCK_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobAuslieferlistePDFDruck();
			}
			if (JOBTYPE_4VENDINGXMLEXPORT_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new Automatikjob4VendingExport();
			}
			if (JOBTYPE_NACHTRAEGLICH_GEOEFFNETE_LOSE_ERLEDIGEN_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobNachtraeglichGeoeffneteLosErledigen();
			}
			if (JOBTYPE_ARBEITSZEITSTATUSPDF_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobArbeitszeitstatusPDFDruck();
			}
			if (JOBTYPE_WARENEINGANGSJOURNALPDF_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobWEJournal();
			}
			if (JOBTYPE_ARTIKELLIEFERANT_WEBABFRAGE_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobWebabfrageArtikellieferant();
			}
			if(JOBTYPE_KPIREPORTPDFDRUCK_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobKpiPdfDruck();
			}
			if (JOBTYPE_BEDARFSUEBERNAHMEOFFENJOURNAL_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobBedarfsuebernahmeOffene();
			}
			if (JOBTYPE_MAILVERSAND_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobMailversand();
			}
			if (JOBTYPE_MAILIMAPABLAGE_TYPE.equals(automatikjobtypeDto.getCJobtype())) {
				return new AutomatikjobMailAblageIMAP();
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

	private void processJobs() {
		Automatiktimer autoTimer = null;
		try {
			autoTimer = em.find(Automatiktimer.class, new Integer(0));
		} catch (Exception e) {
			myLogger.error("Automatiktimer ID=0 konnte nicht gefunden werden", e);
			return;
		}
		
		final Time performHour = autoTimer == null 
				? new Time(System.currentTimeMillis()) 
						: autoTimer.getTTimetoperform();

		HvCreatingCachingProvider<String,Boolean> isNonWorkingDaysCache = new HvCreatingCachingProvider<String, Boolean>() {
			protected Boolean provideValue(String key, String transformedKey) {
				return checkNonWorkingDay(performHour, theClientDto);
			}
		};

		lastMandantCnr = null;
		Timestamp tTimeStarted = getTimestamp();
		AutomatikjobDto[] jobs = listActiveJobs();
		for (AutomatikjobDto listedJobDto : jobs) {
			AutomatikjobDto automatikjobDto = null;
			try {
				// Neu laden, koennte sich geaendert haben
				automatikjobDto = getAutomatikjobFac().automatikjobFindByPrimaryKey(listedJobDto.getIId());
				if (!changeToMandant(automatikjobDto.getCMandantCNr())) {
					continue;
				}
				if (!shouldRunJob(automatikjobDto, isNonWorkingDaysCache)) {
					continue;
				}
				
				AutomatikjobBasis automatikjobBasis = getClassForJobType(automatikjobDto.
						getIAutomatikjobtypeIid());
				//ausfuehren des aktuellen Jobs
				if (automatikjobBasis.performJob(theClientDto)) {
					sendMailToUser();
				} else {
					automatikjobDto.setDLastperformed(Helper.cutTimestamp(tTimeStarted));
					automatikjobDto.setDNextperform(Helper.cutTimestamp(new Timestamp(automatikjobDto.
							getDLastperformed().getTime() +
							(automatikjobDto.getIIntervall() *
									loneDayInMillis))));
				}
			} catch (RemoteException e) {
			}
			
			try {
				if (automatikjobDto != null) {
					getAutomatikjobFac().updateAutomatikjob(automatikjobDto);
				}
			} catch (RemoteException ex1) {
				myLogger.error("Fehler beim Beenden des Jobs", ex1);
			}
				
			try {
				getLogonFac().logout(theClientDto);
			} catch (Throwable t) {
				myLogger.error("Fehler beim Abmelden des Benutzers", t);
			}			
		}		
	}

	private boolean shouldRunJob(AutomatikjobDto jobDto,
			HvCreatingCachingProvider<String, Boolean> isNonWorkingDaysCache) {
		//Wenn der Monatsletzte ist und es ein Monatsjob ist fuehre diesen aus
		if (checkLastDayOfMonth()
				&& jobDto.getBMonthjob() != 0) {
			return true;
		}
		
		if (isNonWorkingDaysCache.getValueOfKey(theClientDto.getMandant())
				&& jobDto.getBPerformOnNonWOrkingDays() == 0) {
			return false;
		}
		
		//Abfrage ob der Intervall bis zur naechsten ausfuehrung stimmt
		if(jobDto.getDLastperformed() == null){
			//Job wurde noch nie durchgefuehrt...
			jobDto.setDLastperformed(new Timestamp(0));
		}
		long intervalTime = loneDayInMillis * jobDto.getIIntervall();
		long lLastperform = jobDto.getDLastperformed().getTime();
		long lLastPlusIntervall = lLastperform + intervalTime;
		Date dLastPlusIntervall = new Date(lLastPlusIntervall);
		if (! (dLastPlusIntervall.equals(getDate()) ||
				dLastPlusIntervall.before(getDate()))) {
			return false;
		}
		
		return true;
	}
	
	
}
