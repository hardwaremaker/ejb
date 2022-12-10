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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.lp.server.personal.service.NachrichtenFac;
import com.lp.server.system.ejb.MailProperty;
import com.lp.server.system.ejb.MailPropertyQuery;
import com.lp.server.system.ejb.Versandanhang;
import com.lp.server.system.ejb.Versandauftrag;
import com.lp.server.system.mail.service.LPMail;
import com.lp.server.system.mail.service.LPMailDto;
import com.lp.server.system.mail.service.MailProcessorFac;
import com.lp.server.system.mail.service.MailTestMessage;
import com.lp.server.system.mail.service.MailTestMessageResult;
import com.lp.server.system.service.MailServiceParameterSource;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.util.Helper;

@Stateless
public class MailProcessorFacBean extends Facade implements MailProcessorFac {

	@PersistenceContext
	private EntityManager em;
	@Resource
	javax.ejb.TimerService timerService;
	@Resource(name = "Mail", mappedName = "java:/Mail")
	javax.mail.Session mailSession;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public javax.ejb.TimerHandle addMail(LPMailDto lpMailDto) {
		javax.ejb.TimerHandle handle = null;
		try {
			javax.ejb.Timer timer = timerService.createTimer(new Date(lpMailDto.getTSendezeitpunktwunsch().getTime()),
					lpMailDto);
			handle = timer.getHandle();
			myLogger.info("Queued ID:" + lpMailDto.toLogInfo());
		} catch (IllegalArgumentException e) {
			myLogger.error(e.getMessage(), e);
		} catch (IllegalStateException e) {
			myLogger.error(e.getMessage(), e);
		} catch (EJBException e) {
			myLogger.error(e.getMessage(), e);
		}
		return handle;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void killTimer(javax.ejb.TimerHandle handle) {
		Timer timer = handle.getTimer();
		if (timer.getInfo() instanceof LPMailDto) {
			timer.cancel();
			myLogger.info("Timer kill ID:" + ((LPMailDto) timer.getInfo()).getVersandauftragIId());
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
				if (((LPMailDto) timer.getInfo()).getVersandauftragIId().longValue() == versandauftragIId.longValue()) {
					try {
						timer.cancel();
						myLogger.info("Timer kill ID:" + versandauftragIId);
					} catch (NoSuchObjectLocalException e) {
						myLogger.error(e.getMessage(), e);
					} catch (IllegalStateException e) {
						myLogger.error(e.getMessage(), e);
					} catch (EJBException e) {
						myLogger.error(e.getMessage(), e);
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
				if (((LPMailDto) timer.getInfo()).getVersandauftragIId().longValue() == versandauftragIId.longValue()) {
					try {
						LPMailDto lpMailDto = (LPMailDto) timer.getInfo();
						timer.cancel();
						lpMailDto.setTSendezeitpunktwunsch(tSendezeitpunktwunsch);
						myLogger.info("Timer kill ID:" + versandauftragIId);
						handle = this.addMail(lpMailDto);
						break;
					} catch (NoSuchObjectLocalException e) {
						myLogger.error(e.getMessage(), e);
					} catch (IllegalStateException e) {
						myLogger.error(e.getMessage(), e);
					} catch (EJBException e) {
						myLogger.error(e.getMessage(), e);
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
		myLogger.info("Timer timeout ID:" + lpMailDto.getVersandauftragIId());
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, lpMailDto.getVersandauftragIId());
		if (versandauftrag != null) {
			versandauftrag = sendMail(versandauftrag, lpMailDto);
			em.merge(versandauftrag);
			em.flush();

			// PJ20063
			if (versandauftrag.getStatusCNr() != null
					&& versandauftrag.getStatusCNr().equals(VersandFac.STATUS_FEHLGESCHLAGEN)) {
				getNachrichtenFac().nachrichtErstellen(NachrichtenFac.Art.EMAIL_VERSAND_FEHLGESCHLAGEN,
						"Email-Versand mit dem Betreff -" + versandauftrag.getCBetreff() + "-" + " fehlgeschlagen",
						versandauftrag.getCStatustext(), null, null, lpMailDto.getTheClientDto());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Versandauftrag sendMail(Versandauftrag versandauftrag, LPMailDto lpMailDto) {
		String smtpServer = lpMailDto.getSmtpServer();
		String smtpBenutzer = lpMailDto.getSmtpBenutzer();
		String smtpKennwort = lpMailDto.getSmtpKennwort();
		if (smtpServer != null) {
			if (smtpServer.length() > 0) {
				// LPMail mail = new LPMail(smtpServer);
				LPMail mail = new LPMail(getMailSession(lpMailDto.getTheClientDto()));
				Message message;
				List<String> anhaenge = new ArrayList<String>();
				try {
					File f = null;
					if (versandauftrag.getOInhalt() != null && versandauftrag.getOInhalt().length > 0) {
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
					String bccEmpfaenger = null;
					if (lpMailDto.isFax()) {
						switch (lpMailDto.getFaxAnbindung()) {
						case LPMail.FAXANBINDUNG_DOMAIN:
							cEmpfaenger = HelperServer.formatFaxnummerFuerMail(versandauftrag.getCEmpfaenger(),
									lpMailDto.getFaxDomain());
							break;
						case LPMail.FAXANBINDUNG_XPIRIO:
							cEmpfaenger = HelperServer.formatFaxnummerFuerMail("send", lpMailDto.getFaxDomain());
							cBetreff = lpMailDto.getXpirioKennwort() + versandauftrag.getCEmpfaenger().replace(" ", "")
									+ ";" + cBetreff;
							break;
						}
						if (versandauftrag.getCAbsenderadresse() == null)
							cAbsender = lpMailDto.getMailAdmin();
						else
							cAbsender = versandauftrag.getCAbsenderadresse();
					} else {
						cEmpfaenger = versandauftrag.getCEmpfaenger();
						cAbsender = versandauftrag.getCAbsenderadresse();
						ccEmpfaenger = versandauftrag.getCCcempfaenger();
						bccEmpfaenger = versandauftrag.getCBccempfaenger();
						// zusaetzliche Anhaenge
						Query query = em.createNamedQuery("VersandanhangfindByVersandauftragIID");
						query.setParameter(1, versandauftrag.getIId());
						List<Versandanhang> versandanhaenge = (List<Versandanhang>) query.getResultList();
						Iterator<Versandanhang> it = versandanhaenge.iterator();
						while (it.hasNext()) {
							Versandanhang anhang = it.next();
							if (anhang.getOInhalt() != null && anhang.getOInhalt().length > 0) {
								f = null;
								try {
									f = HelperServer.bytesToFile(anhang.getOInhalt(), anhang.getCDateiname());
								} catch (IOException e) {
									myLogger.warn(e.getMessage(), e);
								}
								if (f != null)
									anhaenge.add(f.getPath());
							} else {
								// TODO: ghp, warum soll man so einen Versandanhang ueberhaupt abspeichern
								// koennen?
								myLogger.warn("Der Versandanhang (id=" + anhang.getIId()
										+ ") fuer den Versandauftrag (id=" + versandauftrag.getIId()
										+ ") hat keinen Inhalt (null bzw. 0 Laenge)! Ignoriert.");
							}
						}
					}

					message = mail.createMail(cBetreff, versandauftrag.getCText(), null, cEmpfaenger, cAbsender,
							ccEmpfaenger, bccEmpfaenger, anhaenge);
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
									mail.store(imapServer, imapBenutzer, lpMailDto.getImapBenutzerKennwort(),
											lpMailDto.getSentFolder(), message);
								} else {
									if (lpMailDto.getImapAdmin() != null) {
										imapBenutzer = lpMailDto.getImapAdmin();
										mail.store(imapServer, imapBenutzer, lpMailDto.getImapAdminKennwort(),
												lpMailDto.getSentFolder(), message);
									} else {
										myLogger.warn("IMAP Kein Benutzer definiert.");
									}

								}
							} catch (MessagingException e) {
								myLogger.error("IMAP FAIL USER:" + imapBenutzer + " " + versandauftrag.toLogString());
								myLogger.error(e.getMessage(), e);
								versandauftrag.setCStatustext(
										versandauftrag.getCStatustext() + " IMAP Ablage fehlgeschlagen!");
							}
						}
					}
				} catch (MessagingException e) {
					myLogger.error("FAIL " + versandauftrag.toLogString());
					myLogger.error(e.getMessage(), e);
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

	private Properties getMailPropertiesCWertNotNull(TheClientDto theClientDto) {
		List<MailProperty> mailProps = MailPropertyQuery.listByMandantCWertNotNull(em, theClientDto.getMandant());
		Properties properties = new Properties();
		for (MailProperty entity : mailProps) {
			properties.setProperty(entity.getPk().getCNr(), entity.getCWert());
		}
		return properties;
	}

	private Session getMailSession(TheClientDto theClientDto) {
		boolean useDbPropertiesSession = MailServiceParameterSource.DB
				.equals(getParameterFac().getMailServiceParameter(theClientDto.getMandant()));

		return useDbPropertiesSession ? Session.getInstance(getMailPropertiesCWertNotNull(theClientDto)) : mailSession;
	}

	public MailTestMessageResult testMailConfiguration(MailTestMessage testMessage, TheClientDto theClientDto) {
		Session testMailSession = getMailSession(theClientDto);
		SendingMailTester mailTest = new SendingMailTester(theClientDto);
		MailTestMessageResult result = mailTest.doIt(testMailSession, testMessage);

		return result;
	}

	private class SendingMailTester {
		private TheClientDto theClientDto;
		private LPMail lpMail;
		private LPMailDto mailDto;

		public SendingMailTester(TheClientDto theClientDto) {
			this.theClientDto = theClientDto;
		}

		public MailTestMessageResult doIt(Session testMailSession, MailTestMessage testMessage) {
			lpMail = new LPMail(testMailSession);
			mailDto = testMessage.getMailDto();
			addPropertiesToText(testMessage, testMailSession.getProperties());
			MailTestMessageResult result = new MailTestMessageResult(testMessage, testMailSession.getProperties());

			Message message = create(testMessage, result);
			if (!result.isMailCreated())
				return result;

			send(message, result);
			if (!result.isMailSent())
				return result;

			if (Helper.isStringEmpty(testMessage.getMailDto().getImapServer())) {
				return result;
			}

			result.imapExists();
			Store store = connectToStore(result);
			if (!result.isImapConnected())
				return result;

			store(store, message, result);
			close(store);

			return result;
		}

		private void addPropertiesToText(MailTestMessage testMessage, Properties properties) {
			if (!testMessage.isAddMailPropertiesToMessage() || properties == null)
				return;

			StringWriter writer = new StringWriter();
			try {
				properties.store(new PrintWriter(writer), "");
				testMessage.message(testMessage.getMessage() + "\n\n" + writer.toString());
			} catch (IOException e) {
				myLogger.error("Adding properties to message error.", e);
			} finally {
				try {
					if (writer != null)
						writer.close();
				} catch (IOException ex) {
					myLogger.error("Closing properties writer error.", ex);
				}
			}
		}

		private void store(Store store, Message message, MailTestMessageResult result) {
			try {
				myLogger.info("Storing testmail in IMAP folder '" + mailDto.getSentFolder() + "'...");
				lpMail.store(store, mailDto.getSentFolder(), message);
				myLogger.info("Storing testmail in IMAP folder '" + mailDto.getSentFolder() + "' succeeded.");
				result.mailStored();
			} catch (Throwable e) {
				myLogger.error("Storing testmail error.", e);
				List<String> folderList = new ArrayList<String>();
				try {
					Folder[] folders = store.getDefaultFolder().list("*");
					for (Folder folder : folders) {
						folderList.add(folder.getFullName());
					}
					myLogger.info("IMAP folders: " + StringUtils.join(folders, ", "));
				} catch (Throwable e2) {
					myLogger.error("Listing IMAP folders error.", e);
				}
				result.mailStorageFailed(e, folderList);
			}
		}

		private Store connectToStore(MailTestMessageResult result) {
			Store store = null;
			try {
				myLogger.info("Connecting to IMAP host '" + mailDto.getImapServer() + "', user '"
						+ mailDto.getImapAdmin() + "'...");
				store = lpMail.connectToStore(mailDto.getImapServer(), mailDto.getImapAdmin(),
						mailDto.getImapAdminKennwort());
				myLogger.info("Connecting to IMAP host succeeded.");
				result.imapConnected();
			} catch (Throwable e) {
				myLogger.error("Connecting to IMAP host error.", e);
				result.imapConnectionFailed(e);
				close(store);
			}
			return store;
		}

		private void send(Message message, MailTestMessageResult result) {
			try {
				myLogger.info("Sending testmail over SMTP host '" + mailDto.getSmtpServer() + "', user '"
						+ mailDto.getSmtpBenutzer() + "'...");
				lpMail.send(mailDto.getSmtpServer(), mailDto.getSmtpBenutzer(), mailDto.getSmtpKennwort(), message);
				myLogger.info("Sending testmail succeeded.");
				result.mailSent();
			} catch (MessagingException e) {
				myLogger.error("Sending testmail error.", e);
				result.sendingMailFailed(e);
			}
		}

		private Message create(MailTestMessage testMessage, MailTestMessageResult result) {
			try {
				Message message = lpMail.createMail(testMessage.getSubject(), testMessage.getMessage(), null,
						testMessage.getTo(), testMessage.getFrom(), null, null, new ArrayList<String>());
				result.mailCreated();
				return message;
			} catch (Throwable e) {
				myLogger.error("Creating mail error.", e);
				result.mailCreationFailed(e);
				return null;
			}
		}

		private void close(Store store) {
			try {
				if (store != null)
					store.close();
			} catch (Throwable e) {
				myLogger.error("Closing imap store error.", e);
			}
		}
	}
}
