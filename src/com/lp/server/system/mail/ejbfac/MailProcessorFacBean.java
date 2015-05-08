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
package com.lp.server.system.mail.ejbfac;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.ejb.Versandanhang;
import com.lp.server.system.ejb.Versandauftrag;
import com.lp.server.system.mail.service.LPMail;
import com.lp.server.system.mail.service.LPMailDto;
import com.lp.server.system.mail.service.MailProcessorFac;
import com.lp.server.system.service.VersandFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.util.Helper;

@Stateless
public class MailProcessorFacBean extends Facade implements MailProcessorFac {

	@PersistenceContext	private EntityManager em;
	@Resource javax.ejb.TimerService timerService;
	@Resource(name="Mail", mappedName="java:/Mail") javax.mail.Session mailSession;

	private static final long serialVersionUID = -4661172202340260421L;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public javax.ejb.TimerHandle addMail(LPMailDto lpMailDto) {
		javax.ejb.TimerHandle handle = null;
		try {
			javax.ejb.Timer timer = timerService.createTimer(new Date(lpMailDto.getTSendezeitpunktwunsch().getTime()), lpMailDto);
			handle = timer.getHandle();
			if (myLogger.isDebugEnabled())
				myLogger.info("Queued ID:" + lpMailDto.toLogInfo());
		} catch (IllegalArgumentException e) {
			myLogger.error(e.getMessage());
		} catch (IllegalStateException e) {
			myLogger.error(e.getMessage());
		} catch (EJBException e) {
			myLogger.error(e.getMessage());
		}	
		return handle;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void killTimer(javax.ejb.TimerHandle handle) {
		Timer timer = handle.getTimer();
		if (timer.getInfo() instanceof LPMailDto) {
			timer.cancel();
			if (myLogger.isDebugEnabled())
				myLogger.info("Timer kill ID:" + ((LPMailDto)timer.getInfo()).getVersandauftragIId());
		}
	}
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void killTimer(Integer versandauftragIId) {
		Collection<javax.ejb.Timer> timers = timerService.getTimers();
		Iterator<javax.ejb.Timer> it = timers.iterator();
		while (it.hasNext()) {
			javax.ejb.Timer timer = it.next();
			if (timer.getInfo() instanceof LPMailDto) {
				if (((LPMailDto)timer.getInfo()).getVersandauftragIId().longValue() == versandauftragIId.longValue()) {
					try {
						timer.cancel();
						if (myLogger.isDebugEnabled())
							myLogger.info("Timer kill ID:" + versandauftragIId);
					} catch (NoSuchObjectLocalException e) {
						myLogger.error(e.getMessage());
					} catch (IllegalStateException e) {
						myLogger.error(e.getMessage());
					} catch (EJBException e) {
						myLogger.error(e.getMessage());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public javax.ejb.TimerHandle updateTimer(Integer versandauftragIId, Timestamp tSendezeitpunktwunsch) {
		Collection<javax.ejb.Timer> timers = timerService.getTimers();
		Iterator<javax.ejb.Timer> it = timers.iterator();
		javax.ejb.TimerHandle handle = null;
		while (it.hasNext()) {
			javax.ejb.Timer timer = it.next();
			if (timer.getInfo() instanceof LPMailDto) {
				if (((LPMailDto)timer.getInfo()).getVersandauftragIId().longValue() == versandauftragIId.longValue()) {
					try {
						LPMailDto lpMailDto = (LPMailDto) timer.getInfo();
						timer.cancel();
						lpMailDto.setTSendezeitpunktwunsch(tSendezeitpunktwunsch);
						if (myLogger.isDebugEnabled())
							myLogger.info("Timer kill ID:" + versandauftragIId);
						handle = this.addMail(lpMailDto);
						break;
					} catch (NoSuchObjectLocalException e) {
						myLogger.error(e.getMessage());
					} catch (IllegalStateException e) {
						myLogger.error(e.getMessage());
					} catch (EJBException e) {
						myLogger.error(e.getMessage());
					}
				}
			}
		}
		return handle;
	}

	
	@Timeout
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void timeout(javax.ejb.Timer timer) {
		System.out.println("MailProcessor.timeout: " + this.hashCode());

		LPMailDto lpMailDto = (LPMailDto) timer.getInfo();
		if (myLogger.isDebugEnabled())
			myLogger.info("Timer timeout ID:" + lpMailDto.getVersandauftragIId());
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, lpMailDto.getVersandauftragIId());
		if (versandauftrag != null) {
			versandauftrag = sendMail(versandauftrag, lpMailDto);
			em.merge(versandauftrag);
			em.flush();
		}
	}

	@SuppressWarnings("unchecked")
	private Versandauftrag sendMail(Versandauftrag versandauftrag, LPMailDto lpMailDto) {
		String smtpServer = lpMailDto.getSmtpServer();
		String smtpBenutzer = lpMailDto.getSmtpBenutzer();
		String smtpKennwort = lpMailDto.getSmtpKennwort();
		if (smtpServer != null) {
			if (smtpServer.length()>0) {
				//LPMail mail = new LPMail(smtpServer);
				LPMail mail = new LPMail(mailSession);
				Message message;
				List<String> anhaenge = new ArrayList();
				try {
					File f = null;
					if (versandauftrag.getOInhalt()!=null && versandauftrag.getOInhalt().length > 0) {
						try {
							f = HelperServer.bytesToFile(versandauftrag.getOInhalt(), versandauftrag.getCDateiname());
						} catch (IOException e) {
							myLogger.warn(e.getMessage());
						}
						if (f != null)
							anhaenge.add(f.getPath());
					}
					
					String cEmpfaenger = null;
					String cAbsender;
					String ccEmpfaenger = null;
					String cBetreff = versandauftrag.getCBetreff();
					if (lpMailDto.isFax()) {
						switch (lpMailDto.getFaxAnbindung()) {
						case LPMail.FAXANBINDUNG_DOMAIN:
							cEmpfaenger = HelperServer.formatFaxnummerFuerMail(versandauftrag.getCEmpfaenger(),
									lpMailDto.getFaxDomain());
							break;
						case LPMail.FAXANBINDUNG_XPIRIO:
							cEmpfaenger = HelperServer.formatFaxnummerFuerMail("send",
									lpMailDto.getFaxDomain());
							cBetreff = lpMailDto.getXpirioKennwort() + versandauftrag.getCEmpfaenger().replace(" ", "") + ";" + cBetreff;
							break;
						}
						if (versandauftrag.getCAbsenderadresse() == null)
							cAbsender = lpMailDto.getMailAdmin();
						else
							cAbsender = versandauftrag.getCAbsenderadresse();
					} else {
						cEmpfaenger = versandauftrag.getCEmpfaenger();
						cAbsender = versandauftrag.getCAbsenderadresse();
						ccEmpfaenger =versandauftrag.getCCcempfaenger();
						//zusaetzliche Anhaenge
						Query query = em.createNamedQuery("VersandanhangfindByVersandauftragIID");
						query.setParameter(1, versandauftrag.getIId());
						List<Versandanhang> versandanhaenge = (List<Versandanhang>) query.getResultList(); 
						Iterator<Versandanhang> it = versandanhaenge.iterator();
						while (it.hasNext()) {
							Versandanhang anhang = it.next();
							if (anhang.getOInhalt().length > 0) {
								f = null;
								try {
									f = HelperServer.bytesToFile(anhang.getOInhalt(), anhang.getCDateiname());
								} catch (IOException e) {
									myLogger.warn(e.getMessage());
								}
								if (f != null)
									anhaenge.add(f.getPath());
							}
						}
					}
					
					message = mail.createMail(cBetreff,	versandauftrag.getCText(), null, 
							cEmpfaenger, cAbsender, ccEmpfaenger, null,  anhaenge);
					if (Helper.short2boolean(versandauftrag.getBEmpfangsbestaetigung()) == true) {
						if (versandauftrag.getCAbsenderadresse() != null) {
							message.addHeader("Disposition-Notification-To", versandauftrag.getCAbsenderadresse());
							message.addHeader("Return-Receipt-To", versandauftrag.getCAbsenderadresse()); 
						}
					}
					mail.send(smtpServer, smtpBenutzer, smtpKennwort, message);
					myLogger.info("OK " + versandauftrag.toLogString());
					versandauftrag.setOInhalt(null);
					anhaengeLoeschen(versandauftrag.getIId());
					versandauftrag.setTSendezeitpunkt(new Timestamp(System.currentTimeMillis()));
					versandauftrag.setStatusCNr(VersandFac.STATUS_ERLEDIGT);
					versandauftrag.setCStatustext("Nachricht versandt.");
					
					String imapServer = lpMailDto.getImapServer();
					if (imapServer != null) {
						// Ablegen in den versandten Mails
						if (imapServer.length() > 0) {
							String imapBenutzer = "";
							try {
								if (lpMailDto.getImapBenutzer() != null) {
									imapBenutzer = lpMailDto.getImapBenutzer();
									mail.store(imapServer, imapBenutzer, lpMailDto.getImapBenutzerKennwort(), lpMailDto.getSentFolder(), message);
								} else {
									if (lpMailDto.getImapAdmin() != null) {
										imapBenutzer = lpMailDto.getImapAdmin();
										mail.store(imapServer, imapBenutzer, lpMailDto.getImapAdminKennwort(), lpMailDto.getSentFolder(), message);
									} else {
										myLogger.warn("IMAP Kein Benutzer definiert.");
									}
										
								}
							} catch (MessagingException e) {
								myLogger.error("IMAP FAIL USER:" + imapBenutzer + " " + versandauftrag.toLogString());
								myLogger.error(e.getMessage());
								versandauftrag.setCStatustext(versandauftrag.getCStatustext() + " IMAP Ablage fehlgeschlagen!");
							}
						}
					}
				} catch (MessagingException e) {
					myLogger.error("FAIL " + versandauftrag.toLogString());
					myLogger.error(e.getMessage());
					versandauftrag.setStatusCNr(VersandFac.STATUS_FEHLGESCHLAGEN);
					versandauftrag.setCStatustext(e.getMessage());
				}
			}
		}
		return versandauftrag;
	}

	@SuppressWarnings("unchecked")
	private void anhaengeLoeschen(Integer versandauftragIId) {
		Query query = em.createNamedQuery("VersandanhangfindByVersandauftragIID");
		query.setParameter(1, versandauftragIId);
		List<Versandanhang> versandanhaenge = (List<Versandanhang>) query.getResultList(); 
		Iterator<Versandanhang> it = versandanhaenge.iterator();
		while (it.hasNext()) {
			Versandanhang anhang = it.next();
			anhang.setOInhalt(null);
			em.merge(anhang);
		}
		em.flush();
	}
}
