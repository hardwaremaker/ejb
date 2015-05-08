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
package com.lp.server.system.mail.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class LPMail {

	public static final int FAXANBINDUNG_DOMAIN = 0;
	public static final int FAXANBINDUNG_XPIRIO = 1;
	
	//private Session session;
	//@Resource(name="mail/Mail")
	private javax.mail.Session mailSession;
	
	public LPMail(String smtpHost) {
		super();
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtpHost);
		mailSession = Session.getDefaultInstance(props);
	}
	
	public LPMail(javax.mail.Session sessionI) {
		mailSession = sessionI;
	}

	
	/**
	 * Wandelt eine Zeichenkette die E-Mail-Adressen enth&auml;lt in ein Array von Objekten um
	 * 
	 * E-Mail Adressen k&ouml;nnen durch ";" voneinander getrennt werden. Es wird eine 
	 * automatische Pr&uuml;fung auf valide Adressen durchgef&uuml;hrt.
	 * 
	 * @param internetAddress ist die Zeichenkette von E-Mail-Adressen. Beispiel: "name@domain.tld;name2@domain;tld"
	 * @return ein Array von InternetAddress
	 * @throws AddressException wird geworfen wenn die E-Mail Adresse syntaktisch falsch ist.
	 */
	public InternetAddress[] getInternetAddresses(String internetAddress) throws AddressException {
		if(null == internetAddress) throw new IllegalArgumentException("internetAddress") ;
		
		String[] recipients = internetAddress.split(";") ;
		
		InternetAddress[] addresses = new InternetAddress[recipients.length] ;
		for(int i = 0; i < recipients.length; i++) {
			addresses[i] = new InternetAddress(recipients[i].trim()) ;
		}
		
		return addresses ;
	}
	
	
	protected void applyRecipientsTo(Message message, RecipientType recipientType, String addresses) throws MessagingException {
		InternetAddress[] emailAddresses = getInternetAddresses(addresses) ;
		for(int i = 0; i < emailAddresses.length; i++) {
			message.addRecipient(recipientType, emailAddresses[i]) ;
		}
	}
	
	
	public Message createMail(String subject, String text, String html, String to, String from, String cc, String bcc, List<String> anhaenge) throws javax.mail.MessagingException {
		//neue Message
		Message message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(from));
		applyRecipientsTo(message, RecipientType.TO, to) ;
		if(cc != null) applyRecipientsTo(message, RecipientType.CC, cc) ;
		if(bcc != null) applyRecipientsTo(message, RecipientType.BCC, bcc) ;
		
		message.setSubject(subject);
		message.setSentDate(new Date(System.currentTimeMillis()));
		
		//Body
 		BodyPart messageBodyPart = new MimeBodyPart();
		if(text == null) {
			text = "" ;
		}
		if(text.startsWith("<html")) {
			((MimeBodyPart)messageBodyPart).setText(text, "utf-8", "html") ;
		} else {
			messageBodyPart.setText(text) ;
		}
		 		
//		if (text == null)
//			messageBodyPart.setText("");
//		else
//			messageBodyPart.setText(text);
		
		Multipart multipart;
		multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		if (anhaenge != null) {
			addAttachments(anhaenge, multipart);
		}
		message.setContent(multipart);

		return message;
	}

	private void addAttachments(List<String> anhaenge, Multipart multipart) throws MessagingException {
		for (int i=0; i<anhaenge.size(); i++) {
			String filename = anhaenge.get(i); 
            MimeBodyPart attachmentBodyPart = new MimeBodyPart(); 
             
            //use a JAF FileDataSource as it does MIME type detection 
            DataSource source = new FileDataSource(filename); 
            attachmentBodyPart.setDataHandler(new DataHandler(source)); 
             
            //assume that the filename you want to send is the same as the 
            //actual file name - could alter this to remove the file path 
            File f = new File(filename);
            String name = f.getName();
            name = name.substring(0, name.lastIndexOf("."));
            attachmentBodyPart.setFileName(name); 
             
            //add the attachment 
            multipart.addBodyPart(attachmentBodyPart); 
        } 
	}
	
	public void store(String imapHost, String user, String password, String sentFolder, Message message) throws MessagingException {
		Store store = mailSession.getStore("imap");
		store.connect(imapHost, user, password);
		Folder folder = store.getFolder(sentFolder);
		folder.open(Folder.READ_WRITE);
		folder.appendMessages(new Message[] {message});
		folder.close(true);
		store.close();
	}
	
	public void send(String smtpHost, String user, String password, Message message) throws javax.mail.MessagingException {
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(smtpHost, user, password);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
}
