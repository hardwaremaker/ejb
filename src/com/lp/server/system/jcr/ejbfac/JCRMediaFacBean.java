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
package com.lp.server.system.jcr.ejbfac;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.mail.MessagingException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.system.jcr.service.JCRMediaAttachment;
import com.lp.server.system.jcr.service.JCRMediaDto;
import com.lp.server.system.jcr.service.JCRMediaDtoAssembler;
import com.lp.server.system.jcr.service.JCRMediaFac;
import com.lp.server.system.jcr.service.JCRMediaUuidHelper;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.medianode.MediaNodeEmail;
import com.lp.server.system.jcr.service.medianode.MediaNodeEmailAttachment;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;

@Stateless
public class JCRMediaFacBean extends Facade implements JCRMediaFac {
	@PersistenceContext
	private EntityManager em;

	private InitialContext ctx;
	private Repository repo;
	private Credentials cred;

	private Session session;
	
	public JCRMediaFacBean() throws NamingException, LoginException, RepositoryException {
		super() ;

		ctx = new InitialContext();
		repo = (Repository) ctx.lookup("java:jcrmedia/local");
		cred = new SimpleCredentials("Anonymous", "".toCharArray());
		session = null ;
	}
	
	public boolean isOnline() {
		return repo != null;
	}

	private Session getSession() throws LoginException, RepositoryException {
		if (null == session) {
			myLogger.info("login to repository necessary");

			if(!isOnline()) {
				myLogger.info("repository not available");
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "repo == null");
			}
			
			session = repo.login(cred);
		}
		myLogger.info("returning session (" + session + ")");
		return session;
	}

	private void closeSession() {
		if (null != session) {
			myLogger.info("logout from repository (" + session + ")");
			try {
				if (session.isLive()) {
					session.logout();
					session = null;
				}
			} catch(IllegalStateException e) {
				myLogger.warn("logout from repository, Illegal State", e);
			}
		}
		session = null;
	}

	private void forcedCloseSession() {
		if (null != session) {
			 myLogger.warn("forced logout from repository (" + session + ")") ;
			
			 session.logout() ;
			 session = null ;
		}
		session = null;
	}
	

	private Node getRootNode() throws RepositoryException {
		Node rootNode = null;
		try {
			rootNode = getSession().getRootNode();
		} catch (IllegalStateException e) {
			forcedCloseSession();
			rootNode = getSession().getRootNode();
		} catch (RepositoryException e) {
			forcedCloseSession();
			rootNode = getSession().getRootNode();
		}
		
		return rootNode ;
	}
	
	public Node getNode(DocPath docPath) throws RepositoryException {
		return getNode(docPath.getPathAsString()) ;
	}
	
	public Node getNode(String docPathString) throws RepositoryException {
		Node rootNode = getRootNode() ;
		Node returnNode = rootNode.getNode(docPathString);
		return returnNode ;
	}
	
	
	private Node updateMediaNode(DocPath docPath, JCRMediaDto mediaDto) throws IOException, MessagingException, RepositoryException {
		Node updateNode = getNode(docPath) ;
		// Neue Version des Knotens
		if (updateNode.getMixinNodeTypes().length == 0)
			updateNode.addMixin("mix:versionable");

		updateNode.checkout();
		setNodeProperties(updateNode, mediaDto);
		updateNode.getParent().save();
		updateNode.checkin();
		
		return updateNode ;
	}
	
	private Node createPathTo(DocPath docPath) throws RepositoryException {
		Node nUpdateNode = null ;
		
		// Der Knoten existiert nicht und muss angelegt werden
		// String[] sFolders = jcrDocDto.getsFullNodePath().split("/");
		List<DocNodeBase> folders = docPath.asDocNodeList();
		String sPath = "";
		for (int i = 0; i < folders.size(); i++) {
			sPath += (i == 0 ? "" : DocPath.SEPERATOR)
					+ folders.get(i).asEncodedPath();

			try {
				nUpdateNode = getNode(sPath);
				try {
					nUpdateNode
							.getProperty(DocNodeBase.NODEPROPERTY_NODETYPE);
				} catch (PathNotFoundException pnfEx) {
					folders.get(i).persist(nUpdateNode);
					nUpdateNode.getParent().save();
				}
			} catch (Exception pnfEx) {
				// Anlegen
				nUpdateNode = getRootNode().addNode(sPath);
				getSession().save();
				folders.get(i).persist(nUpdateNode);
				nUpdateNode.getParent().save();
			}
		}
		
		return nUpdateNode ;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JCRMediaUuidHelper addEMail(JCRMediaDto mediaDto, TheClientDto theClientDto) 
			throws Exception {
		return addEMailCloseSession(mediaDto, theClientDto) ;
	}
	
	@Override
	public JCRMediaUuidHelper addEMailWithinTransaction(JCRMediaDto mediaDto, TheClientDto theClientDto) 
			throws Exception {
		return addEmailImpl(mediaDto, theClientDto);
	}

	@Override
	public JCRMediaUuidHelper addEMailCloseSession(JCRMediaDto mediaDto, TheClientDto theClientDto) 
			throws Exception {
		JCRMediaUuidHelper uuidHelper = addEmailImpl(mediaDto, theClientDto);
		closeSession() ;
		return uuidHelper ;
	}
	
	private JCRMediaDto mediaDtoFrom(JCRMediaDto jcrMediaDto, TheClientDto theClientDto) throws MessagingException {
		jcrMediaDto.setMandantCnr(theClientDto.getMandant());
		return jcrMediaDto ;
	}
	
	
	private void setNodeProperties(Node node, JCRMediaDto mediaDto) throws IOException, MessagingException, RepositoryException {
//		node.setProperty(NNEmail.FROM, mediaDto.getFromAddr()) ;		
//		node.setProperty(NNEmail.TO, mediaDto.getToAddr()) ;
//		node.setProperty(NNEmail.CC, mediaDto.getCcAddr()) ;
//		node.setProperty(NNEmail.BCC, mediaDto.getBccAddr()) ;
//		node.setProperty(NNEmail.SUBJECT, mediaDto.getSubject()) ;
//		node.setProperty(NNEmail.MESSAGEID, mediaDto.getMessageId()) ;
//		node.setProperty(NNEmail.EMAILDATE, mediaDto.getTEmailDate().getTime()) ;
//		node.setProperty(NNEmail.DATA, mediaDto.getContentStream()) ;
		logSetProperty(node, NNEmail.FROM, mediaDto.getFromAddr()) ;		
		logSetProperty(node, NNEmail.TO, mediaDto.getToAddr()) ;
		logSetProperty(node, NNEmail.CC, mediaDto.getCcAddr()) ;
		logSetProperty(node, NNEmail.BCC, mediaDto.getBccAddr()) ;
		logSetProperty(node, NNEmail.SUBJECT, mediaDto.getSubject()) ;
		logSetProperty(node, NNEmail.MESSAGEID, mediaDto.getMessageId()) ;
		logSetProperty(node, NNEmail.INREPLYTOID, mediaDto.getInReplyToId()) ;
		logSetProperty(node, NNEmail.EMAILDATE, mediaDto.getTEmailDate().getTime()) ;
		logSetProperty(node, NNEmail.ISHTML, mediaDto.isHtmlText());
		node.setProperty(NNEmail.DATA, mediaDto.getContentStream()) ;
	}
	
	private void logSetProperty(Node node, String nodename, boolean value) throws RepositoryException {
		myLogger.warn("Setting " + nodename + " to '" + value + "'.");
		node.setProperty(nodename, value) ;
	}

	private void logSetProperty(Node node, String nodename, String value) throws RepositoryException {
		myLogger.warn("Setting " + nodename + " to '" + value + "'.");
		node.setProperty(nodename, value) ;
	}

	private void logSetProperty(Node node, String nodename, long value) throws RepositoryException {
		myLogger.warn("Setting " + nodename + " to '" + value + "'.");
		node.setProperty(nodename, value) ;
	}
	
	private void setAttachmentProperties(Node jcrAttachNode,
		JCRMediaDto mediaDto, JCRMediaAttachment attachment) throws IOException, RepositoryException {
//		jcrAttachNode.setProperty(NNAttachment.PARENTUUID, mediaDto.getJcrUuid()) ;
//		jcrAttachNode.setProperty(NNAttachment.NAME, attachment.getName()) ;
//		jcrAttachNode.setProperty(NNAttachment.DESCRIPTION,  attachment.getDescription()) ;
//		jcrAttachNode.setProperty(NNAttachment.TYPE, attachment.getMimeType()) ;
//		jcrAttachNode.setProperty(NNAttachment.DATA, attachment.getContentStream()) ;
		logSetProperty(jcrAttachNode, NNAttachment.PARENTUUID, mediaDto.getJcrUuid()) ;
		logSetProperty(jcrAttachNode, NNAttachment.NAME, attachment.getName()) ;
		logSetProperty(jcrAttachNode, NNAttachment.DESCRIPTION,  attachment.getDescription()) ;
		logSetProperty(jcrAttachNode, NNAttachment.TYPE, attachment.getMimeType()) ;
		jcrAttachNode.setProperty(NNAttachment.DATA, attachment.getContentStream()) ;
	}
	
	private JCRMediaUuidHelper addEmailImpl(JCRMediaDto mediaDto, TheClientDto theClientDto) throws Exception {
		Validator.notNull(mediaDto, "mediaDto");
		Validator.notNull(theClientDto, "theClientDto");
		
		mediaDtoFrom(mediaDto, theClientDto) ;
		MediaNodeEmail mediaNodeEmail = new MediaNodeEmail(mediaDto) ;
		DocPath docPath = new DocPath(mediaNodeEmail) ;
		
//		Node updateNode = null;
//		try {
//			updateNode = updateMediaNode(docPath, jcrMediaDto);
//		} catch (PathNotFoundException ex) {
		try {
			Node updateNode = createPathTo(docPath) ;
			updateNode.addMixin("mix:versionable");
			setNodeProperties(updateNode, mediaDto);
			updateNode.save();

			JCRMediaUuidHelper uuidHelper = new JCRMediaUuidHelper(updateNode.getUUID()) ;
			
			for (JCRMediaAttachment attachment : mediaDto.getAttachments()) {
				MediaNodeEmailAttachment attachNode = 
						new MediaNodeEmailAttachment(attachment, mediaDto) ;
				DocPath attachPath = new DocPath(attachNode) ;
				
				Node jcrAttachNode = createPathTo(attachPath) ;
				jcrAttachNode.addMixin("mix:versionable");
//				jcrAttachNode.addMixin("mix:referenceable");
				setAttachmentProperties(jcrAttachNode, mediaDto, attachment) ;
				jcrAttachNode.save() ;
				jcrAttachNode.checkin() ;
				String uuid = jcrAttachNode.getUUID() ;
				myLogger.info("Attachment-Node-UUID: " + uuid);
				uuidHelper.addAttachmentUuid(uuid) ;
			}

			// Auch gleich die erste Version anlegen
//			updateNode.checkout();
//			setNodeProperties(updateNode, jcrMediaDto);
//			updateNode.save();
			updateNode.checkin();
			
			String uuid = updateNode.getUUID() ;
			myLogger.info("EMail-Node-UUID: " + uuid);
			
			if(getSession().hasPendingChanges()) {
				myLogger.info("Pending changes for node " + uuid + ".");
				getSession().save() ;
			}
			return uuidHelper ;
		} catch(Exception e) {
			myLogger.error("addEmail-Exception", e) ;
			throw e ;
		}		
	}

	public JCRMediaDto findEmail(String messageId,
			TheClientDto theClientDto) throws IOException, MessagingException {
		return findEmail(messageId, false, theClientDto) ;
	}
	
	public JCRMediaDto findEmail(String messageId, boolean withContent,
			TheClientDto theClientDto) throws IOException, MessagingException {
		try {
			return findEmailImpl(messageId, withContent, theClientDto) ;
		} catch(RepositoryException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler. Login an JCR fehlgeschlagen oder kein root-Node vorhanden");			
		} finally {
			closeSession();
		}
	}

	public JCRMediaDto findEmailEx(String messageId, boolean withContent, 
			TheClientDto theClientDto) throws IOException, RepositoryException, MessagingException {
		try {
			return findEmailImpl(messageId, withContent, theClientDto) ;
		} finally {
			closeSession();
		}
	}
	
	
	protected JCRMediaDto findEmailImpl(String messageId, boolean withContent, 
			TheClientDto theClientDto) throws IOException, RepositoryException, MessagingException{
		Validator.notEmpty(messageId, "messageId");
		JCRMediaDto mediaDto = mediaDtoFrom(new JCRMediaDto(), theClientDto) ;
		mediaDto.setMessageId(messageId) ;
		MediaNodeEmail mediaNodeEmail = new MediaNodeEmail(mediaDto) ;
		DocPath docPath = new DocPath(mediaNodeEmail) ;

		Node findNode = getNode(docPath) ;
		return JCRMediaDtoAssembler.createDto(findNode, withContent) ;
	}
	
	public JCRMediaDto findEmailByUUID(String uuid,
			TheClientDto theClientDto) throws IOException, RepositoryException {
		return findEmailByUUID(uuid, false, theClientDto) ;
	}
	
	public JCRMediaDto findEmailByUUID(String uuid, boolean withContent,
			TheClientDto theClientDto) throws IOException, RepositoryException {
		Validator.notEmpty(uuid, "uuid");
		
		try {
			Node n = getSession().getNodeByUUID(uuid) ;	
//			return new JCRMediaDto(n, withContent) ;
			return JCRMediaDtoAssembler.createDto(n, withContent) ;
		} catch(IllegalStateException e) {
			forcedCloseSession() ; 
			Node n = getSession().getNodeByUUID(uuid) ;	
//			return new JCRMediaDto(n, withContent) ;
			return JCRMediaDtoAssembler.createDto(n, withContent) ;
		} catch(RepositoryException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler. Login an JCR fehlgeschlagen oder kein root-Node vorhanden");
		} finally {
			closeSession() ;
		}
	}
	
//	@Override
//	public void retrieveEmails(TheClientDto theClientDto) throws IOException,
//			MessagingException, RepositoryException {
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
//		int totalMessages = folder.getMessageCount() ;
//		if(totalMessages == 0) {
//			myLogger.info("No (new) messages found in folder " + folder.getFullName() + ".") ;
//			return ;
//		}
//		
//		for(int i = 1 ; i <= totalMessages; i++) {
//			Message m = null ;
//			try {
//				m = folder.getMessage(i) ;
//
//				String messageId = null ;
//				try {
//					messageId = ((MimeMessage) m).getMessageID() ;
//					if(messageId == null) {
//						messageId = JCRMediaDto.getGeneratedMessageId() ;
//					}
//				} catch(MessagingException e) {
//					if(e.getMessage() != null && e.getMessage().toLowerCase()
//							.contains("failed to load imap envelope")) {
//						ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
//						m.writeTo(bos) ;
//						bos.close();
//						SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray()) ;
//						MimeMessage newMessage = new MimeMessage(session, bis) ;
//						bis.close();
//						m = newMessage ;
//						messageId = ((MimeMessage) m).getMessageID() ;
//						if(messageId == null) {
//							messageId = JCRMediaDto.getGeneratedMessageId() ;
//						}
//					}
//				}
//				
//				JCRMediaDto mediaDto = null ;
//				try {
//					mediaDto = findEmailImpl(messageId, true, theClientDto) ;
//					myLogger.warn("Already in media base with uuid: " + mediaDto.getJcrUuid() +
//							" for messageId: "+ ((MimeMessage) m).getMessageID() + " Subject: " + m.getSubject()) ;
//				} catch(RepositoryException e) {
//					mediaDto = JCRMediaDtoAssembler.createDto((MimeMessage)m, true) ;
//					String uuid = addEMailWithinTransaction(mediaDto, theClientDto) ;
//					mediaDto.setJcrUuid(uuid);
//					myLogger.warn("Created new Msg with uuid: " + uuid + "for messageId: "+ 
//					((MimeMessage) m).getMessageID() + " Subject: " + m.getSubject()) ;
//				}
//				
//				MediaEmailMeta emailMeta = null ;
//				try {
//					emailMeta = MediaEmailMetaQuery.findByUuid(em, mediaDto.getJcrUuid()) ;
//				} catch(NoResultException e) {
//					emailMeta = new MediaEmailMeta() ;
//					emailMeta.setBOnline(Helper.getShortTrue());
//					emailMeta.setCFrom(mediaDto.getFromAddr());
//					emailMeta.setCTo(mediaDto.getCcAddr() != null ? mediaDto.getCcAddr() : "");
//					emailMeta.setCCc(mediaDto.getCcAddr());
//					emailMeta.setCBcc(mediaDto.getBccAddr()) ;
//					emailMeta.setCSubject(mediaDto.getSubject());
//					emailMeta.setMandantCNr(theClientDto.getMandant());
//					emailMeta.setTEmailDate(mediaDto.getTEmailDate());
//					emailMeta.setTAnlegen(getTimestamp());
//					emailMeta.setUuid(mediaDto.getJcrUuid());
//					byte[] bytes = mediaDto.getContent() ;
//					if(bytes != null && bytes.length > 0) {
//						emailMeta.setXContent(new String(bytes, 0, Math.min(2048, bytes.length)));
//					}
//										
//					em.persist(emailMeta);
//					em.flush();					
//				}
//
//				if(emailMeta != null && emailMeta.getIId() != null) {
//					try {
//						MediaInboxQuery.findByMediaIdPersonalId(em, emailMeta.getIId(), theClientDto.getIDPersonal()) ;
//					} catch(NoResultException e) {
//						MediaInbox inbox = new MediaInbox() ;
//						inbox.setBVersteckt(Helper.getShortFalse());
//						inbox.setMediaId(emailMeta.getIId()) ;
//						inbox.setPersonalIId(theClientDto.getIDPersonal());
//						inbox.setStatusCnr("Angelegt");
//						Timestamp t = getTimestamp() ;
//						inbox.setTAendern(t) ;
//						inbox.setTAnlegen(t);
//						inbox.setPersonalIIdAendern(theClientDto.getIDPersonal());
//						inbox.setPersonalIIdAnlegen(theClientDto.getIDPersonal()) ;
//						
//						em.persist(inbox) ;
//						em.flush(); 
//					}
//				}
//				
//				myLogger.warn("(" + i + ") MessageId: " + ((MimeMessage) m).getMessageID() + " Subject: " + m.getSubject()) ;
//			} catch(IndexOutOfBoundsException e) {
//				myLogger.info("OutOfBounds", e) ;
//			} catch(Exception e) {
//				myLogger.error("(" + i + ") Exception during read of email:", e) ;				
//			}
//		}
//		
//		folder.close(false);
//		store.close();
//	}
}
