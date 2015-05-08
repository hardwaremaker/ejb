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
package com.lp.server.system.jcr.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeUtility;

import com.lp.server.util.Validator;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.sun.mail.imap.IMAPMessage;

public class JCRMediaDtoAssembler {
	private final static ILPLogger myLogger = LPLogService.getInstance().getLogger(JCRMediaDtoAssembler.class);

	public static String getGeneratedMessageId() {
		return "<HELIUMV." + UUID.randomUUID() + "@localhost>" ;
	}
	
	/**
	 * Ein JCRMediaDto aus einem JCR Node erzeugen. Der Content wird ignoriert.
	 * 
	 * @param node
	 * @return aus dem JCR Node erzeugtes Dto
	 */
	public static JCRMediaDto createDto(Node node) throws ValueFormatException, RepositoryException, IOException {
		return createDto(node, false) ;
	}
	
	public static JCRMediaDto createDto(Node node, boolean withContent) throws ValueFormatException, RepositoryException, IOException {
		Validator.notNull(node, "node");

		JCRMediaDto dto = new JCRMediaDto() ;
		
		if (withContent) {
			dto.setContent(node.getProperty(JCRMediaFac.NNEmail.DATA).getStream()) ;
		}
			
		dto.setFromAddr(node.getProperty(JCRMediaFac.NNEmail.FROM).getString());
		dto.setToAddr(node.hasProperty(JCRMediaFac.NNEmail.TO) ? node.getProperty(JCRMediaFac.NNEmail.TO).getString() : null);
		dto.setCcAddr(node.hasProperty(JCRMediaFac.NNEmail.CC) ? node.getProperty(JCRMediaFac.NNEmail.CC).getString() : null);
		dto.setBccAddr(node.hasProperty(JCRMediaFac.NNEmail.BCC) ? node.getProperty(JCRMediaFac.NNEmail.BCC).getString() : null);
		dto.setSubject(node.hasProperty(JCRMediaFac.NNEmail.SUBJECT) ? node.getProperty(JCRMediaFac.NNEmail.SUBJECT).getString() : "");
		dto.setMessageId(node.getProperty(JCRMediaFac.NNEmail.MESSAGEID).getString());
		if(node.hasProperty(JCRMediaFac.NNEmail.EMAILDATE)) {
			dto.setTEmailDate(new Timestamp(node.getProperty(JCRMediaFac.NNEmail.EMAILDATE).getLong()));			
		}
		
		dto.setJcrUuid(node.getUUID()) ;		
		return dto ;
	}
	
	  /**
     * Return the primary text content of the message.
     */
    private static String getText(JCRMediaDto dto, 
    		Part p) throws MessagingException, IOException {
    	if(p.isMimeType("text/calendar")) {
    		Object o = p.getContent() ;
    		return "calendar wird derzeit noch nicht unterstuetzt" ;
    	}
    	
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            dto.setTextIsHtml(p.isMimeType("text/html")) ;
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(dto, bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(dto, bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(dto, bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(dto, mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

    private static void handleAttachments(JCRMediaDto dto, Part p) throws MessagingException, IOException {
    	if(!p.isMimeType("multipart/*")) return ;
    	
    	int count = 0 ;    	
    	Multipart mp = (Multipart) p.getContent() ;
    	for(int i = 0 ; i < mp.getCount(); i++) {
    		Part part = mp.getBodyPart(i) ;
    		
    		myLogger.warn("Part " + i + "(" + Part.ATTACHMENT.equals(part.getDisposition()) 
    				+ ") filename: " + part.getFileName() + ".") ;

    		if(part.getFileName() != null && !part.getFileName().isEmpty()) {    			
    			String decodedFilename = MimeUtility.decodeText(part.getFileName()) ;
    			
    			if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
    				String decodedDescription = "" ;
    				if(part.getDescription() != null && !part.getDescription().isEmpty()) {
    					decodedDescription = MimeUtility.decodeText(part.getDescription()) ;
    				}
    				
    				myLogger.warn("Having attachment description " + part.getDescription()) ;
    				
    				JCRMediaAttachment attachment = new JCRMediaAttachment(decodedFilename, decodedDescription) ;
    				attachment.setId(++count + "") ;
    				attachment.setContent(readInto(part.getInputStream()));
    				attachment.setMimeType(part.getContentType());
    				dto.getAttachments().add(attachment) ;
    			}
    		}
    	}
    }

    private static byte[] readInto(InputStream inputStream) throws IOException {
    	ByteArrayOutputStream buffer = new ByteArrayOutputStream(16384) ;
    	
    	byte[] tmpData = new byte[16384] ;
    	int readbytes ;
    	while((readbytes = inputStream.read(tmpData, 0, tmpData.length)) != -1) {
    		buffer.write(tmpData, 0, readbytes) ;
    	}
    	buffer.flush() ;
    	return buffer.toByteArray() ;
    }
    
    private static String getAddressString(Address[] address) {
    	StringBuffer s = new StringBuffer() ;
    	if(address != null && address.length > 0) {
        	for (Address a : address) {
    			if(s.length() > 0) {
    				s.append("; ") ;
    			}
    			s.append(a.toString()) ;
     		}    		
    	}
    	
    	return s.toString() ;
    }
    

	/**
	 * Ein JCRMediaDto erzeugen, der Inhalt der Message wird ignoriert
	 * 
	 * @param message die MimeMessage ohne dabei den Body auszuwerten
	 * 
	 * @return ein JCRMediaDto  
	 * @throws EJBExceptionLP wenn message null
	 */
	public static JCRMediaDto createDto(MimeMessage message) throws IOException, MessagingException {
		return createDto(message, false) ;
	}
	
	public static JCRMediaDto createDto(MimeMessage message, boolean withContent) throws IOException, MessagingException {
		Validator.notNull(message, "message");
		
		JCRMediaDto dto = new JCRMediaDto() ;
		if(withContent) {
			String s = getText(dto, message) ;
			if(s != null) {
				dto.setContent(s.getBytes()) ;
			}
			
			handleAttachments(dto, message) ;
		}
		
		Address from[] = message.getFrom() ;
		if(from != null && from.length > 0) {
			dto.setFromAddr(from[0].toString()) ;			
		}
		
		dto.setToAddr(getAddressString(message.getRecipients(RecipientType.TO)));
		dto.setCcAddr(getAddressString(message.getRecipients(RecipientType.CC)));
		dto.setBccAddr(getAddressString(message.getRecipients(RecipientType.BCC)));
		if(message instanceof IMAPMessage) {
			try {
				dto.setInReplyToId(((IMAPMessage)message).getInReplyTo()) ;
			} catch(MessagingException e) {
				myLogger.warn("Ignored MessageException while getting inReplyTo()", e) ;
			}
		}
		Date d = message.getReceivedDate() ;
		if(d != null) {
			dto.setTEmailDate(new Timestamp( message.getReceivedDate().getTime())) ;			
		} else {
			dto.setTEmailDate(new Timestamp(System.currentTimeMillis())) ;
		}
		dto.setSubject(message.getSubject()) ;
		String messageId = message.getMessageID() ;
		dto.setMessageId(messageId == null ? getGeneratedMessageId() : messageId);	
		return dto ;
	}
	
}
