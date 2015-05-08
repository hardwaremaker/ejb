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
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jcr.RepositoryException;
import javax.jms.JMSException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.lp.server.media.ejb.MediaEmailAttachment;
import com.lp.server.media.ejb.MediaEmailAttachmentQuery;
import com.lp.server.media.ejb.MediaEmailMeta;
import com.lp.server.media.ejb.MediaEmailMetaQuery;
import com.lp.server.media.ejb.MediaInbox;
import com.lp.server.media.ejb.MediaInboxQuery;
import com.lp.server.media.service.HvProgressQueueSender;
import com.lp.server.system.jcr.service.JCRMediaAttachment;
import com.lp.server.system.jcr.service.JCRMediaDto;
import com.lp.server.system.jcr.service.JCRMediaDtoAssembler;
import com.lp.server.system.jcr.service.JCRMediaUuidHelper;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

@Stateless
public class EmailMediaLocalFacBean extends Facade implements EmailMediaLocalFac {
	@PersistenceContext
	private EntityManager em ;
	
	private JCRMediaDto getReplyJcrMediaDto(String replyId, TheClientDto theClientDto) {
		try {
			if(replyId != null) {
				return getJCRMediaFac().findEmailEx(replyId, false, theClientDto) ;
			}
		} catch(RepositoryException e) {
			myLogger.warn("Ignored ReplyId not found in JCR for '" + replyId + "'", e);
		} catch(MessagingException e) {
			myLogger.warn("Ignored ReplyId msgexc for '" + replyId + "'", e) ;
		} catch(IOException e) {
		}
		
		return null ;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void retrieveOneEmail(IMailserver mailserver, int messagenum, 
			HvProgressQueueSender progressQueue, TheClientDto theClientDto)  throws IOException,
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
			JCRMediaDto replyDto = null ;
			try {
				mediaDto = getJCRMediaFac().findEmailEx(messageId, true, theClientDto) ;
				myLogger.warn("Already in media base with uuid: " + mediaDto.getJcrUuid() +
						" for messageId: "+ ((MimeMessage) m).getMessageID() + " Subject: " + m.getSubject()) ;
				
				replyDto = getReplyJcrMediaDto(mediaDto.getInReplyToId(), theClientDto) ;
			} catch(RepositoryException e) {
				mediaDto = JCRMediaDtoAssembler.createDto((MimeMessage)m, true) ;
				JCRMediaUuidHelper uuidHelper = getJCRMediaFac().addEMailCloseSession(mediaDto, theClientDto) ;
//				String uuid = getJCRMediaFac().addEMailWithinTransaction(mediaDto, theClientDto) ;
//				String uuid = getJCRMediaFac().addEMail(mediaDto, theClientDto) ;
				uuidHelper.setMediaDto(mediaDto);
				myLogger.warn("Created new Msg with uuid: " + mediaDto.getJcrUuid() + "for messageId: "+ 
						((MimeMessage) m).getMessageID() + " Subject: " + m.getSubject()) ;	
				
				replyDto = getReplyJcrMediaDto(mediaDto.getInReplyToId(), theClientDto) ;
			}
			
			if(messagenum % 5 == 1) {
				progressQueue.postInfo(mediaDto.getSubject()) ;
			}

			MediaEmailMeta emailMeta = null ;
			try {
				emailMeta = MediaEmailMetaQuery.findByUuid(em, mediaDto.getJcrUuid()) ;
			} catch(NoResultException e) {

				MediaEmailMeta replyMeta = null ;
				if(replyDto != null) {
					try {
						replyMeta = MediaEmailMetaQuery.findByUuid(em, replyDto.getJcrUuid()) ;
					} catch(NoResultException re) {
						myLogger.warn("Ignored could not find reply-emailmeta for uuid '" + replyDto.getJcrUuid() + "'.", re);
					}
				}
				emailMeta = new MediaEmailMeta() ;
				emailMeta.setBOnline(Helper.getShortTrue());
				emailMeta.setCFrom(mimedecoded(mediaDto.getFromAddr()));
				emailMeta.setCTo(mediaDto.getToAddr() != null ? mimedecoded(mediaDto.getToAddr()) : "");
				emailMeta.setCCc(mimedecoded(mediaDto.getCcAddr()));
				emailMeta.setCBcc(mimedecoded(mediaDto.getBccAddr())) ;
				emailMeta.setCSubject(mimedecoded(mediaDto.getSubject()));
				emailMeta.setMandantCNr(theClientDto.getMandant());
				emailMeta.setTEmailDate(mediaDto.getTEmailDate());
				emailMeta.setTAnlegen(getTimestamp());
				emailMeta.setUuid(mediaDto.getJcrUuid());
				emailMeta.setReplyMediaIId(replyMeta != null ? replyMeta.getIId() : null);
				emailMeta.setBHtml(Helper.boolean2Short(mediaDto.isHtmlText())) ;
				byte[] bytes = mediaDto.getContent() ;
				if(bytes != null && bytes.length > 0) {
					emailMeta.setXContent(new String(bytes, 0, Math.min(2048, bytes.length)));
				}
									
				em.persist(emailMeta);
				em.flush();					
			}

			if(emailMeta != null && emailMeta.getIId() != null) {
				for (JCRMediaAttachment attachment : mediaDto.getAttachments()) {
					MediaEmailAttachment emailAttachment ;
					try {
						emailAttachment = MediaEmailAttachmentQuery.findByUuid(em, attachment.getUuid()) ;
					} catch(NoResultException e) {
						emailAttachment = new MediaEmailAttachment() ;
						emailAttachment.setMediaIId(emailMeta.getIId());
						emailAttachment.setCUuid(attachment.getUuid());
						emailAttachment.setCDescription(attachment.getDescription());
						emailAttachment.setCMimeType(attachment.getMimeType());
						emailAttachment.setCName(attachment.getName());
						emailAttachment.setSize(attachment.getContent().length);
						
						em.persist(emailAttachment);
						em.flush() ;
					}
				}
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
			myLogger.warn("OutOfBounds", e) ;
		} catch(Exception e) {
			myLogger.error("(" + messagenum + ") Exception during read of email:", e) ;				
		} catch(Throwable t) {
			myLogger.error("Throwable", t) ;
		}
	}
	
	private String mimedecoded(String encoded) throws UnsupportedEncodingException {
		if(encoded == null) return encoded ;
		
		return MimeUtility.decodeText(encoded) ;
	}
}
