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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import javax.mail.util.SharedByteArrayInputStream;

import com.lp.server.personal.service.PersonalDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.Helper;

public class MailserverImap implements IMailserver {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());
	private Store store = null ;
	private Session session = null ;
	private Folder inboxFolder = null ;
	private Folder movedFolder = null ;
	private String imapHost ;
	private PersonalDto personalDto ;
	
	public MailserverImap(String imapHost, PersonalDto personalDto) {
		this.imapHost = imapHost ;
		this.personalDto = personalDto ;
	}
	
	protected String getProtocol() {
		return "imap" ;
	}
	
	public void connect() throws MessagingException {
		Properties props = System.getProperties() ;
		session = Session.getDefaultInstance(props, null) ;
		session.setDebug(true);

		store = session.getStore(getProtocol()) ;
		
		try {
			int atIndex = personalDto.getCImapbenutzer().indexOf('|') ;
			if(atIndex != -1) {
				int protocolIndex = personalDto.getCImapbenutzer().indexOf('|', atIndex + 1) ;
				if(protocolIndex != -1) {
					String protocol = personalDto.getCImapbenutzer().substring(atIndex + 1, protocolIndex) ;
					store = session.getStore(protocol) ;
					atIndex = protocolIndex ;
				}
				String host = personalDto.getCImapbenutzer().substring(atIndex + 1) ;
				String user = personalDto.getCImapbenutzer().substring(0, atIndex) ;
//				store.connect(host, 143, user, personalDto.getCImapkennwort());
				store.connect(host, user, personalDto.getCImapkennwort());
			} else {
				store.connect(imapHost, personalDto.getCImapbenutzer(), personalDto.getCImapkennwort());
			}
		} catch(Exception e) {
			myLogger.error("Exception", e);
		}
		inboxFolder = null ;
	}
	
	@Override
	public void disconnect() throws MessagingException {
		if(inboxFolder != null) {
			inboxFolder.close(false) ;
		}
		
		if(store != null) {
			store.close() ;
		}
	}

	@Override
	public void disconnect(boolean expunge) throws MessagingException {
		if(inboxFolder != null) {
			inboxFolder.close(expunge) ;
		}
		
		if(store != null) {
			store.close() ;
		}
	}
	
	protected String getInboxFoldername() {
		return Helper.isStringEmpty(personalDto.getCImapInboxFolder()) 
				? "INBOX" : personalDto.getCImapInboxFolder().trim();
	}
	
	protected String getMovedFoldername() {
		return "Moved" ;
	}
		
	protected Store getStore() {
		return store ;
	}
	
	protected Session getSession() {
		return session ;
	}
		
	protected Folder getInboxFolder() throws MessagingException {
		if(inboxFolder == null) {
			
//			getFolderList() ;
			
			Folder folder = store.getDefaultFolder() ;
			if(folder == null) {
				myLogger.warn("Could not find the store defaultFolder") ;
				return null ;
			}
			
			folder = folder.getFolder(getInboxFoldername()) ;
			if(folder == null) {
				myLogger.warn("Could not find the mbox " + getInboxFoldername() + ".") ;
				return null ;
			}
			
			try {
				folder.open(Folder.READ_WRITE) ;
			} catch(MessagingException e) {
				folder.open(Folder.READ_ONLY) ;
			}

			inboxFolder = folder ;
		}
		
		return inboxFolder ;
	}

	private void listFolders(Folder[] folders) throws MessagingException {
		for (Folder folder : folders) {
			myLogger.warn("Folder: " + folder.getFullName()) ;
//			if(folder.exists() && (folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
//			}
		}		
	}
	
	protected void getFolderList() throws MessagingException {
		Folder[] allFolders = store.getDefaultFolder().list("*") ;
		listFolders(allFolders) ;
		
		Folder[] personalFolders = store.getPersonalNamespaces() ;
		listFolders(personalFolders) ;
		
		Folder[] sharedFolders = store.getSharedNamespaces() ;
		listFolders(sharedFolders) ;
	}
	
	protected Folder getMovedFolder() throws MessagingException {
		if(movedFolder == null) {
			Folder folder = store.getDefaultFolder() ;
			if(folder == null) {
				myLogger.warn("Could not find the store defaultFolder") ;
				return null ;
			}
			
			folder = folder.getFolder(getMovedFoldername()) ;
			if(folder == null) {
				myLogger.warn("Could not find the mbox " + getMovedFoldername() + ".") ;
				return null ;
			}
			
			try {
				folder.open(Folder.READ_WRITE) ;
			} catch(MessagingException e) {
				folder.open(Folder.READ_ONLY) ;
			}

			movedFolder = folder ;
		}
		
		return movedFolder ;
	}
	

	public int getInboxMessageCount() throws MessagingException {
		Folder f = getInboxFolder() ;
		if(f == null) return -1 ;
		
		return f.getMessageCount() ;
	}
	
	
	@Override
	public Message getInboxMessage(int messageNumber) throws IOException, MessagingException {
		Folder f = getInboxFolder() ;
		if(f == null) return null ;
		
		Message m = f.getMessage(messageNumber) ;
		try {
			String messageId = ((MimeMessage) m).getMessageID() ;
		} catch(MessagingException e) {
			if(e.getMessage() != null && e.getMessage().toLowerCase()
					.contains("failed to load imap envelope")) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
				m.writeTo(bos) ;
				bos.close();
				SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray()) ;
				MimeMessage newMessage = new MimeMessage(getSession(), bis) ;
				bis.close();
				m = newMessage ;
			}			
		}
		
		return m ;
	}
	
	@Override
	public boolean removeMessage(String messageId) throws MessagingException {
		Folder f = getInboxFolder() ;
		if(f == null) return false ;
		
		Folder movedFolder = getMovedFolder() ;
		if(movedFolder == null) return false ;
		
		SearchTerm searchTerm = new MessageIDTerm(messageId) ;
		Message[] messages = f.search(searchTerm) ;
		
		boolean found = messages != null && messages.length == 1 ;		
		if(found) {
	        f.copyMessages(messages, movedFolder) ;
	        f.setFlags(messages, new Flags(Flags.Flag.DELETED), true);			
		}
		
		return found ;
	}
}
