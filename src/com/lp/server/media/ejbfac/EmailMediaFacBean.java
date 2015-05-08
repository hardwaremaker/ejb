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
 *******************************************************************************/
package com.lp.server.media.ejbfac;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jcr.RepositoryException;
import javax.jms.JMSException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.modelmapper.ModelMapper;

import com.lp.server.media.ejb.MediaEmailAttachment;
import com.lp.server.media.ejb.MediaEmailAttachmentQuery;
import com.lp.server.media.ejb.MediaEmailMeta;
import com.lp.server.media.ejb.MediaEmailMetaQuery;
import com.lp.server.media.ejb.MediaInbox;
import com.lp.server.media.ejb.MediaInboxQuery;
import com.lp.server.media.ejb.MediaStoreBeleg;
import com.lp.server.media.ejb.MediaStoreBelegQuery;
import com.lp.server.media.service.EmailMediaFac;
import com.lp.server.media.service.HvCancelQueueReceiver;
import com.lp.server.media.service.HvProgressQueueSender;
import com.lp.server.media.service.MediaEmailAttachmentDto;
import com.lp.server.media.service.MediaEmailMetaDto;
import com.lp.server.media.service.MediaStoreBelegDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.system.jcr.service.JCRMediaDto;
import com.lp.server.system.jcr.service.JCRMediaDtoAssembler;
import com.lp.server.system.jcr.service.JCRMediaUuidHelper;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class EmailMediaFacBean extends Facade implements EmailMediaFac {
	@PersistenceContext
	private EntityManager em ;

	private HvProgressQueueSender progressQueue ;
	private HvCancelQueueReceiver cancelQueue ;
	private HvCancelSignal cancelSignal ;
	
	public EmailMediaFacBean() {
		progressQueue = new HvProgressQueueSender(EmailMediaFac.PROGRESS_QUEUENAME) ;
	}
	
	private void createCancelQueueReceiver() {
		if(cancelQueue == null) {
			cancelSignal = new HvCancelSignal() ;
			cancelQueue = new HvCancelQueueReceiver(EmailMediaFac.CANCEL_QUEUENAME, cancelSignal) ;
			myLogger.info("Created new cancelQueueReceiver " + cancelQueue + " with signal " + cancelSignal + "."); 
		}
	}
	
	
	public MediaEmailMetaDto emailMetaFindByInboxId(Integer inboxId) throws EJBExceptionLP {
		Validator.notNull(inboxId, "inboxId") ;

//		try {
//			progressQueue.postInfo("emailMetaFindByInboxId " + inboxId + ".") ;
//		} catch (JMSException e) {
//			myLogger.warn("JMSException", e);
//		}
		
		MediaInbox mediaInbox = (MediaInbox) em.find(MediaInbox.class, inboxId) ;
		if(mediaInbox == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, inboxId.toString()) ;
		}

		MediaEmailMeta emailMeta = (MediaEmailMeta) em.find(MediaEmailMeta.class, mediaInbox.getMediaId()) ;
		List<MediaEmailAttachment> emailAttachments = null ;
		if(emailMeta != null) {
			emailAttachments = MediaEmailAttachmentQuery.listByMediaId(em, mediaInbox.getMediaId()) ;
		}
		return assembleMediaEmailMetaDto(emailMeta, emailAttachments) ;
	}
	
	
	public MediaEmailMetaDto emailMetaFindByMediaId(Integer mediaId) throws EJBExceptionLP {
		Validator.notNull(mediaId, "mediaId") ;
		MediaEmailMeta emailMeta = (MediaEmailMeta) em.find(MediaEmailMeta.class, mediaId) ;
		List<MediaEmailAttachment> emailAttachments = null ;
		if(emailMeta != null) {
			emailAttachments = MediaEmailAttachmentQuery.listByMediaId(em, mediaId) ;
		}
		return assembleMediaEmailMetaDto(emailMeta, emailAttachments) ;
	}
	
	
	public void removeInboxEntry(Integer inboxId) throws EJBExceptionLP {
		Validator.notNull(inboxId, "inboxId");

		MediaInbox mediaInbox = (MediaInbox) em.find(MediaInbox.class, inboxId);
		if (mediaInbox == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, inboxId.toString());
		}
		
		try {
			em.remove(mediaInbox);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void versteckeInboxEntry(Integer inboxId) throws EJBExceptionLP {
		Validator.notNull(inboxId, "inboxId");

		MediaInbox mediaInbox = (MediaInbox) em.find(MediaInbox.class, inboxId);
		if (mediaInbox == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, inboxId.toString());
		}
		
		mediaInbox.setBVersteckt(Helper.getShortTrue());
		em.merge(mediaInbox);
		em.flush();
	}
	
	public void moveInboxEntryTo(Integer inboxId, Integer personalId, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.notNull(inboxId, "inboxId");
		Validator.notNull(personalId, "personalId");
		
		MediaInbox mediaInbox = (MediaInbox) em.find(MediaInbox.class, inboxId);
		if (mediaInbox == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, inboxId.toString());
		}
		mediaInbox.setBVersteckt(Helper.getShortTrue());
		Timestamp t = getTimestamp() ;
		mediaInbox.setTGelesen(t) ;
		em.merge(mediaInbox);
		em.flush();
		
		MediaInbox toMediaInbox = new MediaInbox() ;
		toMediaInbox.setMediaId(mediaInbox.getMediaId());
		toMediaInbox.setBVersteckt(Helper.getShortFalse());
		toMediaInbox.setPersonalIId(personalId);
		toMediaInbox.setPersonalIIdAnlegen(theClientDto.getIDPersonal()); 
		toMediaInbox.setPersonalIIdAendern(theClientDto.getIDPersonal()) ;
		toMediaInbox.setTAnlegen(t);
		toMediaInbox.setTAendern(t);
		toMediaInbox.setStatusCnr(LocaleFac.STATUS_ANGELEGT);
		em.persist(toMediaInbox);
		em.flush() ;
	}
	
	private MediaEmailMetaDto assembleMediaEmailMetaDto(
			MediaEmailMeta emailMeta, List<MediaEmailAttachment> emailAttachments) {
		ModelMapper mapper = new ModelMapper() ;
		MediaEmailMetaDto dto = mapper.map(emailMeta, MediaEmailMetaDto.class) ;
		dto.setCCc(emailMeta.getCCc());

		List<MediaEmailAttachmentDto> attachmentDtos = new ArrayList<MediaEmailAttachmentDto>();
		if(emailAttachments != null) {
			for (MediaEmailAttachment emailAttachment : emailAttachments) {
				MediaEmailAttachmentDto attachmentDto =
						mapper.map(emailAttachment, MediaEmailAttachmentDto.class) ;
				attachmentDtos.add(attachmentDto) ;
			}
		}
		dto.setAttachments(attachmentDtos);
		return dto ;
	}
	
//	@Override
//	public void onMessage(javax.jms.Message message) {
//		try {
//			TextMessage msg = (TextMessage) message;
//			String command = msg.getText() ;
//			myLogger.warn("received command: " + command);
//			msg.acknowledge();
//
//			if (command.startsWith("cancel:")) {
//				if(command.endsWith(EmailMediaFac.CancelMethod.RETRIEVE_EMAIL)) {
//					getCancelSignal().triggerCancel(); 
////					cancelRetrieveEmail = true ;
//					myLogger.warn("cancelRetrieveEmail acknowledged");
//				}
//			}
//		} catch (JMSException e) {
//			myLogger.error("JMSException", e);
//		}
//	}
	
	public void removeEmail(String uuid, TheClientDto theClientDto) throws IOException, MessagingException {
		MediaEmailMeta emailMeta = null ;
		try {
			emailMeta = MediaEmailMetaQuery.findByUuid(em, uuid) ;
		} catch(NoResultException e) {
			return ;
		}
		if(emailMeta.getBOnline() < 1) {
			return ;
		}
		
		JCRMediaDto mediaDto = null ;
		try {
			mediaDto = getJCRMediaFac().findEmailByUUID(uuid, false, theClientDto);			
		} catch(RepositoryException e) {
			return ;
		}
		String jcrMsgId = mediaDto.getMessageId() ;
		
		IMailserver mailServer = getMailserver(theClientDto) ;
		mailServer.connect();
		mailServer.removeMessage(jcrMsgId); 
		mailServer.disconnect(true); 
		
//		Properties props = System.getProperties() ;
//		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null) ;
//		
//		String url = "imap://Gerold:ghp.logp.66@mail.logistikpur.local/" ;
//		Store store = session.getStore(new URLName(url)) ;
//		store.connect();
//		
//		Folder folder = store.getDefaultFolder() ;
//		if(folder == null) {
//			myLogger.warn("Could not find the Store defaultFolder") ;
//			return ;
//		}
//		
//		String mbox = "INBOX" ;
//		folder = folder.getFolder(mbox) ;
//		if(folder == null) {
//			myLogger.warn("Could not find the mbox " + mbox + ".") ;
//			return ;
//		}
//		
//		try {
//			folder.open(Folder.READ_WRITE) ;
//		} catch(MessagingException e) {
//			folder.open(Folder.READ_ONLY) ;
//		}
//
//		String mboxMoved = "Moved" ;
//		Folder movedFolder = store.getDefaultFolder().getFolder(mboxMoved) ;
//		if(movedFolder == null) {
//			myLogger.warn("Could not find the folder " + mboxMoved + ".");
//		}
//		try {
//			if(!movedFolder.exists()) {
//				movedFolder.create(Folder.HOLDS_MESSAGES);
//			}
//			movedFolder.open(Folder.READ_WRITE) ;
//		} catch(MessagingException e) {
//			movedFolder.open(Folder.READ_ONLY) ;
//		}
//	
//		try {
//			SearchTerm searchTerm = new MessageIDTerm(jcrMsgId) ;
//			Message[] messages = folder.search(searchTerm) ;
//			if(messages != null && messages.length == 1) {
//		        folder.copyMessages(messages, movedFolder) ;
//		        folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
//				
//			}
//		} catch(MessagingException e) {
//			myLogger.warn("MessagingException:", e) ;
//		}
//		
//		folder.close(false);
//		store.close();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void retrieveOneEmail(IMailserver mailserver, int messagenum, TheClientDto theClientDto)  throws IOException,
		MessagingException, RepositoryException, JMSException  {
		
		try {
			if(messagenum % 5 == 1) {
				progressQueue.postValue(messagenum) ;
			}
			
			Message m = mailserver.getInboxMessage(messagenum) ;
			String messageId = ((MimeMessage)m).getMessageID() ;
			if(messageId == null){
				messageId = JCRMediaDto.getGeneratedMessageId() ;
			}
			
			JCRMediaDto mediaDto = null ;
			try {
				mediaDto = getJCRMediaFac().findEmailEx(messageId, true, theClientDto) ;
				myLogger.warn("Already in media base with uuid: " + mediaDto.getJcrUuid() +
						" for messageId: "+ ((MimeMessage) m).getMessageID() + " Subject: " + m.getSubject()) ;
			} catch(RepositoryException e) {
				mediaDto = JCRMediaDtoAssembler.createDto((MimeMessage)m, true) ;
				JCRMediaUuidHelper uuidHelper = getJCRMediaFac().addEMailWithinTransaction(mediaDto, theClientDto) ;
				uuidHelper.setMediaDto(mediaDto);
				myLogger.warn("Created new Msg with uuid: " + uuidHelper.getBaseUuid() + "for messageId: "+ 
						((MimeMessage) m).getMessageID() + " Subject: " + m.getSubject()) ;				
			}
			
			if(messagenum % 5 == 1) {
				progressQueue.postInfo(mediaDto.getSubject()) ;
			}

			MediaEmailMeta emailMeta = null ;
			try {
				emailMeta = MediaEmailMetaQuery.findByUuid(em, mediaDto.getJcrUuid()) ;
			} catch(NoResultException e) {
				emailMeta = new MediaEmailMeta() ;
				emailMeta.setBOnline(Helper.getShortTrue());
				emailMeta.setCFrom(mediaDto.getFromAddr());
				emailMeta.setCTo(mediaDto.getFromAddr() != null ? mediaDto.getFromAddr() : "");
				emailMeta.setCCc(mediaDto.getCcAddr());
				emailMeta.setCBcc(mediaDto.getBccAddr()) ;
				emailMeta.setCSubject(mediaDto.getSubject());
				emailMeta.setMandantCNr(theClientDto.getMandant());
				emailMeta.setTEmailDate(mediaDto.getTEmailDate());
				emailMeta.setTAnlegen(getTimestamp());
				emailMeta.setUuid(mediaDto.getJcrUuid());
				byte[] bytes = mediaDto.getContent() ;
				if(bytes != null && bytes.length > 0) {
					emailMeta.setXContent(new String(bytes, 0, Math.min(2048, bytes.length)));
				}
									
				em.persist(emailMeta);
				em.flush();					
			}

			if(emailMeta != null && emailMeta.getIId() != null) {
				try {
					MediaInboxQuery.findByMediaIdPersonalId(em, emailMeta.getIId(), theClientDto.getIDPersonal()) ;
				} catch(NoResultException e) {
					MediaInbox inbox = new MediaInbox() ;
					inbox.setBVersteckt(Helper.getShortFalse());
					inbox.setMediaId(emailMeta.getIId()) ;
					inbox.setPersonalIId(theClientDto.getIDPersonal());
					inbox.setStatusCnr(LocaleFac.STATUS_ANGELEGT);
					Timestamp t = getTimestamp() ;
					inbox.setTAendern(t) ;
					inbox.setTAnlegen(t);
					inbox.setPersonalIIdAendern(theClientDto.getIDPersonal());
					inbox.setPersonalIIdAnlegen(theClientDto.getIDPersonal()) ;
					
					em.persist(inbox) ;
					em.flush(); 
				}
			}
			
			myLogger.warn("(" + messagenum + ") MessageId: " + ((MimeMessage) m).getMessageID() + " Subject: " + m.getSubject()) ;
		} catch(IndexOutOfBoundsException e) {
			myLogger.info("OutOfBounds", e) ;
		} catch(Exception e) {
			myLogger.error("(" + messagenum + ") Exception during read of email:", e) ;				
		}		
	}
	
	public void createBelegReferenz(Integer mediaId, String belegart, Integer belegId, TheClientDto theClientDto) {
		createBelegReferenz(mediaId, belegart, belegId, null, theClientDto) ;
	}
	
	public void createBelegReferenz(Integer mediaId, String belegart, 
			Integer belegId, Integer belegpositionId, TheClientDto theClientDto) {
		Validator.notNull(mediaId,   "mediaId");
		Validator.notEmpty(belegart, "belegart");
		Validator.notNull(belegId,   "belegId");
		
		try {
			MediaStoreBeleg ref = new MediaStoreBeleg() ;
			ref.setMandantCNr(theClientDto.getMandant());
			ref.setMediaIId(mediaId);
			ref.setCBelegart(belegart);
			ref.setBelegIId(belegId);
			ref.setBelegpositionIId(belegpositionId);
			
			Timestamp t = getTimestamp() ;
			ref.setTAnlegen(t);
			ref.setTAendern(t);
			ref.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			ref.setPersonalIIdAendern(theClientDto.getIDPersonal());
			
			em.persist(ref);
			em.flush() ;
		} catch(EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch(Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}
	
	public void createReferenzPartnerKurzbrief(Integer mediaId, Integer partnerId, TheClientDto theClientDto) {
		createBelegReferenz(mediaId, LocaleFac.BELEGART_PARTNER, partnerId, theClientDto);
	}

	public void createReferenzKundeKurzbrief(Integer mediaId, Integer partnerId, TheClientDto theClientDto) {
		createBelegReferenz(mediaId, LocaleFac.BELEGART_KUNDE, partnerId, theClientDto);
	}
	
	public MediaStoreBelegDto belegReferenzFindByReferenceId(Integer referenceId, TheClientDto theClientDto) {
		return belegReferenzFindByReferenceId(referenceId, false, theClientDto) ;
	}
	
	public MediaStoreBelegDto belegReferenzFindByReferenceId(Integer referenceId, boolean loadEmailMeta, TheClientDto theClientDto) {
		Validator.notNull(referenceId, "referenceId") ;
		
		MediaStoreBeleg storeBeleg = em.find(MediaStoreBeleg.class, referenceId);
		if(storeBeleg == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, referenceId.toString());
		}
		
		MediaStoreBelegDto belegDto = assembleMediaStoreBelegDto(storeBeleg) ;
		
		if(loadEmailMeta && belegDto != null && belegDto.getMediaIId() != null) {
			belegDto.setEmailMetaDto(emailMetaFindByMediaId(belegDto.getMediaIId())) ;
		}
		return belegDto ;
	}
	
	
	private MediaStoreBelegDto assembleMediaStoreBelegDto(
			MediaStoreBeleg storeBeleg) {
		ModelMapper mapper = new ModelMapper() ;
		MediaStoreBelegDto dto = mapper.map(storeBeleg, MediaStoreBelegDto.class) ;
		return dto ;
	}	

	
	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void retrieveEmails(TheClientDto theClientDto) throws IOException,
			MessagingException, RepositoryException, JMSException {
		
		progressQueue.postInfo("Verbindung herstellen...");
		
		IMailserver mailserver = getMailserver(theClientDto) ;
		mailserver.connect() ;
		
		progressQueue.postInfo("Nachrichtenanzahl ermitteln...");
		int totalMessages = mailserver.getInboxMessageCount() ;
		progressQueue.postCount(totalMessages);
		if(totalMessages <= 0) {
			myLogger.info("No (new) messages found.") ;
			mailserver.disconnect() ;
		}
		
//		int[] fixedMsgs = new int[]{5, 8, 10, 12, 14} ;
//		for(int j = 0 ; j < fixedMsgs.length; j++) {
//			try {
//				getEMailMediaLocalFac().retrieveOneEmail(mailserver, fixedMsgs[j], progressQueue, theClientDto) ;
//			} catch(Throwable t) {
//				myLogger.error("Got throwable", t) ;				
//				if(t instanceof RuntimeException) {
//					myLogger.warn("Retrying - because of runtime ex - the message num " + fixedMsgs[j] + ".");
//					try {
//						getEMailMediaLocalFac().retrieveOneEmail(mailserver, fixedMsgs[j], progressQueue, theClientDto) ;
//					} catch(Throwable t2) {
//						myLogger.error("Got throwable 2", t2) ;										
//					}
//				}
//			}
//		}

		cancelQueue = null ;
		createCancelQueueReceiver(); 

//		cancelRetrieveEmail = false ;
		for(int i = 1 ; i <= totalMessages; i++) {
			myLogger.info("cancelQueue triggered: " + cancelSignal.isTriggered() + " " + cancelSignal);
			if(cancelSignal.isTriggered()) break ;
			
			try {
				getEMailMediaLocalFac().retrieveOneEmail(mailserver, i, progressQueue, theClientDto) ;
			} catch(Throwable t) {
				myLogger.error("Got Throwable in message num " + i + ":", t) ;				

				if(t instanceof RuntimeException) {
					myLogger.warn("Retrying - because of runtime ex - the message num " + i + ".");
					try {
						getEMailMediaLocalFac().retrieveOneEmail(mailserver, i, progressQueue, theClientDto) ;
					} catch(Throwable t2) {
						myLogger.error("Got throwable 2", t2) ;										
					}
				}
			}
		}
		
		mailserver.disconnect() ;
	}
	
	
	protected IMailserver getMailserver(TheClientDto theClientDto) throws RemoteException {
		PersonalDto personalDto = getPersonalFac()
				.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto) ;

		String imapServer = getParameterFac()
				.parametermandantFindByPrimaryKey(
						ParameterFac.PARAMETER_IMAPSERVER,
						ParameterFac.KATEGORIE_VERSANDAUFTRAG,
						theClientDto.getMandant()).getCWert();

		return new MailserverImap(imapServer, personalDto) ;
	}
	
	public Integer createKurzbriefFromEmail(Integer partnerId, String belegartCnr, MediaEmailMetaDto emailMetaDto, TheClientDto theClientDto) throws Throwable {
		Validator.notNull(emailMetaDto, "emailMetaDto");
		Validator.notNull(partnerId, "partnerId");
		Validator.notEmpty(belegartCnr, "belegartCnr");
		Validator.notNull(theClientDto, "theClientDto");
		
		String email = sanitizedEmail(emailMetaDto.getCFrom()) ;
		
		KurzbriefDto kurzbriefDto = new KurzbriefDto() ;
		kurzbriefDto.setPartnerIId(partnerId);
		kurzbriefDto.setCBetreff(cuttedText(emailMetaDto.getCSubject(), 80)); 
		kurzbriefDto.setXText(emailMetaDto.getXContent()); 
		kurzbriefDto.setBelegartCNr(belegartCnr);
		kurzbriefDto.setTAendern(emailMetaDto.getTEmailDate()) ;
		kurzbriefDto.setBHtml(emailMetaDto.getBHtml());

		if(email != null) {
			AnsprechpartnerDto[] ansprechpartners = getAnsprechpartnerFac().ansprechpartnerFindByEmail(email, theClientDto) ;
			if(ansprechpartners != null && ansprechpartners.length == 1) {
				kurzbriefDto.setAnsprechpartnerIId(ansprechpartners[0].getIId());
			}			
		}
		
		Integer id = getPartnerFac().createKurzbrief(kurzbriefDto, theClientDto) ;
		
		createBelegReferenz(emailMetaDto.getIId(), belegartCnr, partnerId, id, theClientDto);

		myLogger.info("Created kurzbrief for partner " + 
				kurzbriefDto.getPartnerIId() + " with id " + id + ".");
		
		return id ;
	}
	
	
	public boolean hasKurzbriefEmailReferenz(Integer kurzbriefId, Integer partnerId, TheClientDto theClientDto) {
		Validator.notNull(kurzbriefId, "kurzbriefId");
		Validator.notNull(partnerId, "partnerId");
		Validator.notNull(theClientDto, "theClientDto");
		return hasBelegEmailReferenz(partnerId, kurzbriefId, theClientDto) ;
	}
	
	
	@Override
	public Integer createHistoryFromEmail(Integer projektId,
			MediaEmailMetaDto emailDto, TheClientDto theClientDto) throws Throwable {
		Validator.notNull(projektId, "projektId");
		Validator.notNull(emailDto, "emailDto") ;
		
		HistoryDto historyDto = new HistoryDto();
		historyDto.setProjektIId(projektId);
		historyDto.setXText(emailDto.getXContent());
		historyDto.setTBelegDatum(emailDto.getTEmailDate());
		historyDto.setCTitel(emailDto.getCSubject());
		historyDto.setBHtml(emailDto.getBHtml());
		Integer id = getProjektFac().createHistory(historyDto, theClientDto) ; 
		createBelegReferenz(emailDto.getIId(), LocaleFac.BELEGART_PROJEKT, projektId, id, theClientDto);

		myLogger.info("Created history for projekt " + projektId + " with id " + id + ".") ;
		return id ;
	}
	
	public boolean hasHistoryEmailReferenz(Integer projektId, Integer historyId, TheClientDto theClientDto) {
		Validator.notNull(projektId, "projektId");
		Validator.notNull(historyId, "historyId");
		Validator.notNull(theClientDto, "theClientDto");
		return hasBelegEmailReferenz(projektId, historyId, theClientDto) ;
	}

	private boolean hasBelegEmailReferenz(Integer belegId, Integer positionId, TheClientDto theClientDto) {
		try {
			MediaStoreBeleg mediaBeleg = MediaStoreBelegQuery.findByMandantBelegIdPositionId(em,
				theClientDto.getMandant(), belegId, positionId) ;
			return mediaBeleg != null ;
		} catch(NoResultException e) {
			return false ;
		}		
	}
	
	private String cuttedText(String value, int length) {
		if(value == null) return null ;
		if(value.length() > length) {
			value = value.substring(0, length - 3) + "..." ;
		}
		return value ;
	}
	
	private String sanitizedEmail(String email) {
		if(email == null) return null ;
		if(email.trim().length() == 0) return null ;
		
		int indexBracketB = email.indexOf("<") ;
		if(indexBracketB == -1) return email.trim() ;
		
		int indexBracketE = email.indexOf(">", indexBracketB + 1) ;
		if(indexBracketE == -1) {
			return email.substring(indexBracketB + 1) ; 
		}
		
		return email.substring(indexBracketB + 1, indexBracketE) ;
	}		
}
