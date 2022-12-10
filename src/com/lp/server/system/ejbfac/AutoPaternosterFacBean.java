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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.ejb.Artikellagerplaetze;
import com.lp.server.artikel.ejb.LagerQuery;
import com.lp.server.artikel.ejb.Lagerplatz;
import com.lp.server.artikel.ejb.Paternoster;
import com.lp.server.artikel.ejb.PaternosterQuery;
import com.lp.server.artikel.ejb.Paternostereigenschaft;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.PaternosterDto;
import com.lp.server.artikel.service.PaternosterDtoAssembler;
import com.lp.server.system.automatikjob.AutomatikjobBasis;
import com.lp.server.system.automatikjob.AutomatikjobPaternoster;
import com.lp.server.system.ejb.Automatikjobs;
import com.lp.server.system.ejb.AutomatikjobsQuery;
import com.lp.server.system.ejb.Automatikjobtype;
import com.lp.server.system.ejb.AutomatikjobtypeQuery;
import com.lp.server.system.service.AutoPaternosterFac;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobDtoAssembler;
import com.lp.server.system.service.AutomatiktimerFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AutoPaternosterFacBean extends Facade implements AutoPaternosterFac {

	@Resource javax.ejb.TimerService timerService;

	@PersistenceContext
	private EntityManager em;
	private TheClientDto theClientDto;
	private String lastMandantCnr;

	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(
			AutomatikjobBasis.class);

	@SuppressWarnings("unchecked")
	public Collection<Paternoster> getAllPaternoster()
		throws RemoteException {
		Query query = em.createNamedQuery("PaternosterAll");
		Collection<Paternoster> allPaternoster = null;
		try {
			allPaternoster = (Collection<Paternoster>) query.getResultList();
		} catch (NoResultException ex) {
			//
		}
		return allPaternoster;
	}
	
	public boolean isPaternosterVerfuegbar(){
		Query query = em.createNamedQuery("PaternosterAll");
		Collection c=query.getResultList();
		
		if(c.size()>0){
			return true;
		} else {
			return false;
		}
		
	}
	
	public PaternosterDto paternosterFindByPrimaryKey(Integer id) 
			throws RemoteException {
		Paternoster p = em.find(Paternoster.class, id);
		
		return assemblePaternosterDto(p);
	}
	private PaternosterDto assemblePaternosterDto(Paternoster paternoster) {
		return PaternosterDtoAssembler.createDto(paternoster);
	}
	@SuppressWarnings("unchecked")
	public List<Paternostereigenschaft> getAllPaternosterEigenschaftByPaternosterId(Integer paternosterId) 
		throws RemoteException {
		Query query = em.createNamedQuery("PaternostereigenschaftfindByIPaternosterid");
		query.setParameter(1, paternosterId);
		List<Paternostereigenschaft> pnAttrib = (List<Paternostereigenschaft>) query.getResultList();
		return pnAttrib;
	}

	@Override
	public void paternosterAddArtikel(Integer paternosterIId,
			ArtikelDto artikelDto) throws RemoteException {
		if (! addArtikelToLeanLift(paternosterIId, artikelDto))
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PATERNOSTERKOMMUNIKATIONSFEHLER,"Fehler Paternosterkommunikation");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void paternosterAddArtikelAll(Integer paternosterIId, TheClientDto theClientDto)
			throws RemoteException {

		Query query = em.createNamedQuery("LagerplatzfindAllByPaternosterIId");
		query.setParameter(1, paternosterIId);
		Lagerplatz lp = (Lagerplatz) query.getSingleResult();
		query = em.createNamedQuery("ArtikellagerplaetzefindByLagerplatzIId");
		query.setParameter(1, lp.getIId());
		List<Artikellagerplaetze> list = (List<Artikellagerplaetze>) query.getResultList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Artikellagerplaetze alp = (Artikellagerplaetze) it.next();
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(alp.getArtikelIId(), theClientDto);
			if (artikelDto != null) {
				paternosterAddArtikel(paternosterIId, artikelDto);
			}
		}
		
	}

	@Override
	public void paternosterDelArtikel(Integer paternosterIId,
			ArtikelDto artikelDto) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	private boolean addArtikelToLeanLift(Integer paternosterIId, ArtikelDto artikelDto) 
		throws RemoteException, EJBExceptionLP {
		String liftmsg;
		if (artikelDto.getArtikelsprDto().getCKbez() != null) {
			liftmsg = "*$S" + artikelDto.getCNr().trim()
				+ "$N" + artikelDto.getArtikelsprDto().getCBez().trim()
				+ "$H01" + artikelDto.getArtikelsprDto().getCKbez().trim()
				+ "$";
		} else {
			liftmsg = "*$S" + artikelDto.getCNr().trim()
			+ "$N" + artikelDto.getArtikelsprDto().getCBez().trim()
			+ "$H01" + ""
			+ "$";
		}
		HashMap<String,String> pnAttribMap = getPaternosterParameter(paternosterIId);
		boolean errorInJob = false;
		int waitResponse = 2;
		String dirCmd = "";
		String dirResponse = "";
		if (pnAttribMap.containsKey(AutoPaternosterFac.PT_EIGENSCHAFT_DIR_CMD))
			dirCmd = pnAttribMap.get(AutoPaternosterFac.PT_EIGENSCHAFT_DIR_CMD);
		if (pnAttribMap.containsKey(AutoPaternosterFac.PT_EIGENSCHAFT_DIR_RESPONSE))
			dirResponse = pnAttribMap.get(AutoPaternosterFac.PT_EIGENSCHAFT_DIR_RESPONSE);
		if (pnAttribMap.containsKey(AutoPaternosterFac.PT_EIGENSCHAFT_WAITRESPONSE))
			waitResponse = new Integer(pnAttribMap.get(AutoPaternosterFac.PT_EIGENSCHAFT_WAITRESPONSE));
		
		// parameter pruefen
		if (dirCmd.length() == 0) {
			myLogger.error("Fehler Parameter dirCmd");
			errorInJob = true;
		}
		if (dirResponse.length() == 0) {
			myLogger.error("Fehler Parameter dirResponse");
			errorInJob = true;
		}

		String filename = "HVArtikel";
		String extRequest = ".amd";
		String extrResponse = ".res";
		try {
			FileWriter fstream = new FileWriter(dirCmd + filename + extRequest);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(liftmsg);
			out.flush();
			out.close();
		} catch (IOException ex) {
			myLogger.error("Fehler beim Erstellen Commandfile", ex);
		}
		try {
			Thread.sleep(waitResponse * 1000);
		} catch (InterruptedException ex) {
			myLogger.error("Fehler beim Warten auf Ergebnis", ex);
		}

		File file = new File(dirResponse + filename + extrResponse);
		if (file.exists()) {
			if (file.length() != 0) {
				// OK
				;
			} else {
				myLogger.error("Response File leer");
				errorInJob = true;
			}
		} else {
				myLogger.error("Response File fehlt");
				errorInJob = true;
			}
		return errorInJob;
	}

	public HashMap<String,String> getPaternosterParameter(Integer paternosterIId) 
		throws RemoteException, EJBExceptionLP {
		// get all parameter
		List<Paternostereigenschaft> pnAttrib = getAutoPaternosterFac()
			.getAllPaternosterEigenschaftByPaternosterId(paternosterIId);
		Iterator<Paternostereigenschaft> iterAttrib = pnAttrib.iterator();
		HashMap<String,String> pmap = new HashMap<String,String>();
		while (iterAttrib.hasNext()) {
			Paternostereigenschaft pa = iterAttrib.next();
			pmap.put(pa.getCNr(), pa.getCWert());
		}
		return pmap;
	}

	@Timeout
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void ejbTimeout(javax.ejb.Timer timer) {
		//prepare next Timeout in x Minuten
		timer.cancel();
		int minutes = getPaternosterIntervall();
		long intervall = minutes * 60 * 1000;
		try {
			setTimer(intervall);
		} catch (RemoteException e) {
			myLogger.error(e.getMessage());
		}
		//Check if Automatik is activated
		Integer iEnabled = 0;
		try {
			iEnabled = getAutomatiktimerFac().automatiktimerFindByPrimaryKey(0).getBEnabled();
		}
		catch (Throwable t) {
			myLogger.error("Die Konfiguration f\u00FCr den Timer konnte nicht gefunden werden");
		}
		
		if (iEnabled != 0) {
			processJobs();
		}
		
//		if (iEnabled != 0) {
//			
//			AutomatikjobtypeDto jobtypDto = null;
//			try {
//				jobtypDto = getAutomatikjobtypeFac().automatikjobtypeFindByCJobType(
//						AutomatiktimerFac.JOBTYPE_PATERNOSTERABFRAGE_TYPE);
//			} catch (Throwable t) {
//				myLogger.error("Es konnten kein Paternoster Jobtyp gefunden werden");
//			}
//			if (jobtypDto != null) {
//				Query query = em.createNamedQuery("AutomatikjobfindByIAutomatikjobtypeIid");
//				query.setParameter(1, jobtypDto.getIId());
//				Automatikjobs job = (Automatikjobs) query.getSingleResult();
//	
//				if (Helper.short2boolean(job.getBActive().shortValue())) {
//					myLogger.info("Automatik start Paternoster");
//					AutomatikjobBasis automatikjobBasis = new AutomatikjobPaternoster();
//					if (automatikjobBasis.performJob(null))
//						myLogger.error("Fehler beim Paternoster Job");
//					
//					myLogger.info("Automatik ende Paternoster");
//					//sendMailToUser();
//				} else {
//					job.setDLastperformed(Helper.cutTimestamp(tTimeStarted));
//					job.setDNextperform(Helper.cutTimestamp(new Timestamp(job.
//							getDLastperformed().getTime() +
//							(job.getIIntervall() * 60 * 1000))));
//				}
//				em.merge(job);
//				em.flush();
//			}
//			
//		}
	}
	
	private void processJobs() {
		lastMandantCnr = null;
		AutomatikjobDto[] jobs = listPaternosterJobs();
		for (AutomatikjobDto jobDto : jobs) {
			Timestamp tTimeStarted = getTimestamp();
			if (Helper.isTrue(jobDto.getBActive().shortValue())) {
				myLogger.info("Automatik start Paternoster");
				if (!changeToMandant(jobDto.getCMandantCNr())) {
					continue;
				}
				
				AutomatikjobBasis automatikjobBasis = new AutomatikjobPaternoster();
				if (automatikjobBasis.performJob(theClientDto)) {
					myLogger.error("Fehler beim Paternoster Job");
				} else {
					jobDto.setDLastperformed(Helper.cutTimestamp(tTimeStarted));
					updateTimestamps(jobDto);
				}
				myLogger.info("Automatik ende Paternoster");
			}
		}
	}

	private void updateTimestamps(AutomatikjobDto jobDto) {
		jobDto.setDNextperform(Helper.cutTimestamp(new Timestamp(jobDto.
				getDLastperformed().getTime() +
				(jobDto.getIIntervall() * 60 * 1000))));
		try {
			getAutomatikjobFac().updateAutomatikjob(jobDto);
		} catch (RemoteException ex1) {
			myLogger.error("Fehler beim Beenden des Jobs", ex1);
		}
	}

	private boolean changeToMandant(String newMandantCNr) {
		if (newMandantCNr.equals(lastMandantCnr)) {
			return true;
		}
		
		try {
			theClientDto = getLogonFac().logonIntern(
					getMandantFac().getLocaleDesHauptmandanten(), newMandantCNr);
			lastMandantCnr = newMandantCNr;
			return true;
		} catch (Throwable t) {
			myLogger.error("Der interne Logon in Mandant " + newMandantCNr + " schlug fehl.", t);
			return false;
		}
	}

	private int getPaternosterIntervall() {
		return 60;
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
	
	private AutomatikjobDto[] listPaternosterJobs() {
		Automatikjobtype jobtype = AutomatikjobtypeQuery.resultByCJobType(em, AutomatiktimerFac.JOBTYPE_PATERNOSTERABFRAGE_TYPE);
		if (jobtype == null) {
			myLogger.error("Es konnte kein Paternoster Jobtyp (" + AutomatiktimerFac.JOBTYPE_PATERNOSTERABFRAGE_TYPE + ") gefunden werden");
			return new AutomatikjobDto[]{};
		}
		
		List<Automatikjobs> jobs = AutomatikjobsQuery.listByAutomatikjobtypeIId(em, jobtype.getIId());
		if (jobs.isEmpty()) {
			return new AutomatikjobDto[]{};
		}
		
		return AutomatikjobDtoAssembler.createDtos(jobs);
	}
	
	public Collection<Paternoster> paternosterFindByMandant(String mandantCnr) {
		List<Integer> lagerIIds = LagerQuery.listIIdsByMandantCNr(em, mandantCnr);
		List<Paternoster> paternoster = PaternosterQuery.listByLagerIIds(em, lagerIIds);
		return paternoster;
	}
}
