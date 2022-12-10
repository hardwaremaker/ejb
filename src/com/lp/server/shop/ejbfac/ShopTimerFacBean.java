package com.lp.server.shop.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.personal.service.PersonalDto;
import com.lp.server.shop.service.ShopTimerFac;
import com.lp.server.system.automatikjob.AutomatikjobBasis;
import com.lp.server.system.automatikjob.AutomatikjobEingangsrechnungenXlsImport;
import com.lp.server.system.automatikjob.AutomatikjobMailAblageIMAP;
import com.lp.server.system.automatikjob.AutomatikjobMailversand;
import com.lp.server.system.automatikjob.AutomatikjobWebArtikelAenderungen;
import com.lp.server.system.automatikjob.AutomatikjobWebBestellungenVerarbeiten;
import com.lp.server.system.ejb.Automatikjobs;
import com.lp.server.system.ejb.AutomatikjobsQuery;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobDtoAssembler;
import com.lp.server.system.service.AutomatikjobtypeDto;
import com.lp.server.system.service.AutomatiktimerFac;
import com.lp.server.system.service.AutomatiktimerFac.Scheduler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.AutomatikjobId;
import com.lp.server.util.Facade;

@Stateless
public class ShopTimerFacBean extends Facade implements ShopTimerFac {	
	@Resource 
	TimerService timerService;
	@PersistenceContext
	private EntityManager em;


	private String lastMandantCnr;
	private TheClientDto theClientDto;

	@Timeout
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void ejbTimeout(Timer timer) {
		myLogger.info("ShopTimer start on " + timer.getHandle().toString() + ".");
		setTimer(DEFAULT_DURATION);
		
		iterateJobs();

		myLogger.info("ShopTimer done");
	}
	
	private void iterateJobs() {
		lastMandantCnr = null;
		AutomatikjobDto[] jobs = listActiveJobs();
		for (AutomatikjobDto job : jobs) {
			AutomatikjobId jobId = new AutomatikjobId(job.getIId());
			if(getShopTimerCronJob().isActive(jobId)) {
				continue;
			}

			try {
				// Neu laden, koennte zwischenzeitlich anders sein
				AutomatikjobDto dto = getAutomatikjobFac()
					.automatikjobFindByPrimaryKey(jobId.id());
				if(!shouldRunJob(dto)) {
					continue;
				}
				
				if(!dto.getCMandantCNr().equals(lastMandantCnr)) {
					changeToMandant(dto.getCMandantCNr());
				}
				
				if(getShopTimerCronJob().startJob(jobId)) {
					try {
						process(dto);
					} catch(Throwable t) {
						myLogger.error("Automatikjob " + jobId.toString() + " throwed:", t);
					}
					getShopTimerCronJob().stopJob(jobId);
				} else {
					myLogger.warn("ShopTimer " + jobId.toString() + " is already running. Skipping.");
				}			
			} catch(RemoteException e) {
				
			}			
		}
	}
	
	private boolean shouldRunJob(AutomatikjobDto dto) throws RemoteException {
		if(dto.getDNextperform() == null){
			dto.setDNextperform(new Timestamp(0));
		}
		
		Timestamp now = getTimestamp();
		if (!dto.getDNextperform().before(now)) {
			return false;
		}
		
		return true;
//		if(dto.getDLastperformed() == null){
//			dto.setDLastperformed(new Timestamp(0));
//		}
//		
//		long intervalTime = DEFAULT_DURATION * dto.getIIntervall();
//		long lLastperform = dto.getDLastperformed().getTime();
//		long lLastPlusIntervall = lLastperform + intervalTime;
//		Timestamp dLastPlusIntervall = new Timestamp(lLastPlusIntervall);
//		Timestamp now = getTimestamp();
//		if (!dLastPlusIntervall.before(now)) {
//			return false;
//		}
//		
//		return true;
	}
	
	private void changeToMandant(String newMandantCnr) {
		lastMandantCnr = newMandantCnr;
		try {
			theClientDto = getLogonFac().logonIntern(
				getMandantFac().getLocaleDesHauptmandanten(), lastMandantCnr);
		} catch (Throwable t) {
			myLogger.error("Das wechseln des Mandants schlug fehl", t);
		}
		lastMandantCnr = theClientDto.getMandant();		
	}
	
	private void process(AutomatikjobDto jobDto) {
		myLogger.info("process " + jobDto.getCName() + " (jobid: " + jobDto.getIId() + ")");
		AutomatikjobBasis ejbJob = createJobInstance(jobDto.getIId(), jobDto.
				getIAutomatikjobtypeIid());
		if(ejbJob != null) {
			Timestamp tTimeStarted = getTimestamp();
			if(ejbJob.performJob(theClientDto)) {
				sendMailToUser(jobDto);
			} else {
				jobDto.setDLastperformed(tTimeStarted);
				updateTimestamps(jobDto);
			}
		}
		
//		TheClientDto theClientDto = getLogonFac().logonIntern(new Locale("de", "AT"), "001");
//		try {
//			getAuftragReportFac().printLieferplan(theClientDto);
//			Calendar c = Calendar.getInstance();
//			c.add(10, Calendar.DATE);
//			getAuftragReportFac().printAuszulieferndePositionen(
//					new Date(c.getTime().getTime()), null, AuftragReportFac.REPORT_AUSZULIEFERNDE_POSITIONEN_SORTIERUNG_ARTIKEL, theClientDto);
//			getLogonFac().logout(theClientDto);
//			
////			throw new IllegalArgumentException("I'm a teapot!");
//		} catch(RemoteException e) {
//		}
	}
	
	private void updateTimestamps(AutomatikjobDto jobDto) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(jobDto.getDLastperformed().getTime());
		c.add(Calendar.MINUTE, jobDto.getIIntervall());
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		jobDto.setDNextperform(new Timestamp(c.getTime().getTime()));
		
		try {
			getAutomatikjobFac().updateAutomatikjob(jobDto);
		} catch (RemoteException ex1) {
			myLogger.error("Fehler beim Beenden des Jobs", ex1);
		}
	}
	
	private void sendMailToUser(AutomatikjobDto jobDto) {
		try {
			VersandauftragDto versandauftragDto = new VersandauftragDto();
			versandauftragDto.setOInhalt(null);
			versandauftragDto.setBEmpfangsbestaetigung(new Short("0"));
			String mailtext = "Dieses E-mail benachrichtigt Sie dar\u00FCber, dass ein Fehler w\u00E4hrend der Ausf\u00FChrung des automatischen Jobs '" + jobDto.getCName() + "' aufgetreten ist. Bitte \u00FCberpr\u00FCfen Sie das Logfile ihres HELIUM V Servers.";
			versandauftragDto.setCText(mailtext);
			PersonalDto persDto = getPersonalFac().personalFindByPrimaryKey(
					theClientDto.getIDPersonal(), theClientDto);
			String sAbsender = persDto.getPartnerDto().getCEmail();
			versandauftragDto.setCAbsenderadresse(sAbsender);
			//Empfaenger = Absender da der ZUstaendige User selbst benachrichtigt wird
			versandauftragDto.setCEmpfaenger(sAbsender);
			String sBetreff = "Automatische Jobs HELIUM V - Es ist ein Fehler aufgetreten";
			versandauftragDto.setCBetreff(sBetreff);
			getVersandFac().createVersandauftrag(versandauftragDto,false,theClientDto);
		}
		catch (Throwable t) {
			myLogger.error("Fehler beim Versenden der Fehlerbenachrichtigung", t);
		}
	}
	
	private AutomatikjobBasis createJobInstance(Integer automatikjobId, int jobtype) {
		AutomatikjobtypeDto dto;
		AutomatikjobId jobId = new AutomatikjobId(automatikjobId);
		try {
			dto = getAutomatikjobtypeFac()
				.automatikjobtypeFindByPrimaryKey(jobtype);
			String jobType = dto.getCJobtype();
			if(AutomatiktimerFac.JOBTYPE_BESTELLUNGENVERARBEITEN_TYPE.equals(jobType)) {
				return new AutomatikjobWebBestellungenVerarbeiten(jobId);
			}
			if(AutomatiktimerFac.JOBTYPE_ARTIKELAENDERUNGEN_TYPE.equals(jobType)) {
				return new AutomatikjobWebArtikelAenderungen(jobId);
			}
			if (AutomatiktimerFac.JOBTYPE_ER_XLSIMPORT_TYPE.equals(jobType)) {
				return new AutomatikjobEingangsrechnungenXlsImport();
			}
			if (AutomatiktimerFac.JOBTYPE_MAILVERSAND_TYPE.equals(jobType)) {
				return new AutomatikjobMailversand();
			}
			if (AutomatiktimerFac.JOBTYPE_MAILIMAPABLAGE_TYPE.equals(jobType)) {
				return new AutomatikjobMailAblageIMAP();
			}
		} catch (RemoteException ex) {
			myLogger.error("RemoteException", ex);
		}

		return null;
	}

	private AutomatikjobDto[] listActiveJobs() {
		List<Automatikjobs> jobs = AutomatikjobsQuery
				.listByActiveScheduler(em, true, Scheduler.WEB);
		return AutomatikjobDtoAssembler.createDtos(jobs);
	}

	@Override
	public void setTimer(long expireInMs) {
		myLogger.info("setTimer with " + expireInMs + "ms called.");
		
		try {
			Collection<Timer> timers = timerService.getTimers();
			for (Timer timer : timers) {
				myLogger.info("Cancelling timer " + timer.getHandle().toString());
				timer.cancel();
			}
		} finally {
			if (expireInMs != 0) {
				myLogger.info("creating new Timer with " + expireInMs + "ms.");
				timerService.createTimer(expireInMs, null);
			}
		}
	}
}
