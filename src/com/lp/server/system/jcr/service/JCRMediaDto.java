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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

public class JCRMediaDto implements Serializable {
	private static final long serialVersionUID = 1934183406824623270L;

	private String messageId ;
	private String mandantCnr ;
	private String fromAddr ;
	private String toAddr ;
	private String ccAddr ;
	private String bccAddr ;
	private Timestamp tEmailDate ;
	private Timestamp tAnlegen ;
	private String subject ;
	private byte[] content ;
	private String jcrUuid ;
	private boolean textIsHtml ;
	private List<JCRMediaAttachment> attachments = new ArrayList<JCRMediaAttachment>() ;
	private String inReplyToId ;
	
	public JCRMediaDto() {
	}
	
	public JCRMediaDto(Node node, boolean withData) throws ValueFormatException,
		PathNotFoundException, RepositoryException, IOException {
		if (withData) {
			setContent(node.getProperty(JCRDocFac.PROPERTY_DATA).getStream()) ;
		}
		setFromAddr(node.hasProperty("from") ?  node.getProperty("from").getString() : "");
		setSubject(node.hasProperty("subject") ? node.getProperty("subject").getString() : "");
		setMessageId(node.hasProperty("messageid") ? node.getProperty("messageid").getString() : "");
		setJcrUuid(node.getUUID()) ;
	}
	
 	
    /**
     * Return the primary text content of the message.
     */
    private String getText(Part p) throws
                MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
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
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

    
	
	public void setContent(InputStream stream) throws IOException {
		byte[] b = new byte[stream.available()] ;
		stream.read(b);
		setContent(b) ;
	}
	
	public InputStream getContentStream()  throws IOException {
		byte[] b = getContent() ;
		if(b == null) {
			b = " ".getBytes() ;
		}
		InputStream stream = new ByteArrayInputStream(b);
		return stream ;
	}
	
 	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMandantCnr() {
		return mandantCnr;
	}
	public void setMandantCnr(String mandantCnr) {
		this.mandantCnr = mandantCnr;
	}
	public String getFromAddr() {
		return fromAddr;
	}
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}
	public String getToAddr() {
		return toAddr;
	}
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}
	public String getCcAddr() {
		return ccAddr;
	}
	public void setCcAddr(String ccAddr) {
		this.ccAddr = ccAddr;
	}
	public String getBccAddr() {
		return bccAddr;
	}

	public void setBccAddr(String bccAddr) {
		this.bccAddr = bccAddr;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getJcrUuid() {
		return jcrUuid;
	}

	public void setJcrUuid(String jcrUuid) {
		this.jcrUuid = jcrUuid;
	}

	public Timestamp getTEmailDate() {
		return tEmailDate;
	}

	public void setTEmailDate(Timestamp tEmailDate) {
		this.tEmailDate = tEmailDate;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public boolean isHtmlText() {
		return textIsHtml ;
	}
	
	public void setTextIsHtml(boolean html) {
		textIsHtml = html ;
	}
	
	public List<JCRMediaAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<JCRMediaAttachment> attachments) {
		this.attachments = attachments;
	}

	public static String getGeneratedMessageId() {
		return "<HELIUMV." + UUID.randomUUID() + "@localhost>" ;
	}

	public String getInReplyToId() {
		return inReplyToId;
	}

	public void setInReplyToId(String inReplyToId) {
		this.inReplyToId = inReplyToId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attachments == null) ? 0 : attachments.hashCode());
		result = prime * result + ((bccAddr == null) ? 0 : bccAddr.hashCode());
		result = prime * result + ((ccAddr == null) ? 0 : ccAddr.hashCode());
		result = prime * result + Arrays.hashCode(content);
		result = prime * result
				+ ((fromAddr == null) ? 0 : fromAddr.hashCode());
		result = prime * result
				+ ((inReplyToId == null) ? 0 : inReplyToId.hashCode());
		result = prime * result + ((jcrUuid == null) ? 0 : jcrUuid.hashCode());
		result = prime * result
				+ ((mandantCnr == null) ? 0 : mandantCnr.hashCode());
		result = prime * result
				+ ((messageId == null) ? 0 : messageId.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result
				+ ((tAnlegen == null) ? 0 : tAnlegen.hashCode());
		result = prime * result
				+ ((tEmailDate == null) ? 0 : tEmailDate.hashCode());
		result = prime * result + (textIsHtml ? 1231 : 1237);
		result = prime * result + ((toAddr == null) ? 0 : toAddr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JCRMediaDto other = (JCRMediaDto) obj;
		if (attachments == null) {
			if (other.attachments != null)
				return false;
		} else if (!attachments.equals(other.attachments))
			return false;
		if (bccAddr == null) {
			if (other.bccAddr != null)
				return false;
		} else if (!bccAddr.equals(other.bccAddr))
			return false;
		if (ccAddr == null) {
			if (other.ccAddr != null)
				return false;
		} else if (!ccAddr.equals(other.ccAddr))
			return false;
		if (!Arrays.equals(content, other.content))
			return false;
		if (fromAddr == null) {
			if (other.fromAddr != null)
				return false;
		} else if (!fromAddr.equals(other.fromAddr))
			return false;
		if (inReplyToId == null) {
			if (other.inReplyToId != null)
				return false;
		} else if (!inReplyToId.equals(other.inReplyToId))
			return false;
		if (jcrUuid == null) {
			if (other.jcrUuid != null)
				return false;
		} else if (!jcrUuid.equals(other.jcrUuid))
			return false;
		if (mandantCnr == null) {
			if (other.mandantCnr != null)
				return false;
		} else if (!mandantCnr.equals(other.mandantCnr))
			return false;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (tAnlegen == null) {
			if (other.tAnlegen != null)
				return false;
		} else if (!tAnlegen.equals(other.tAnlegen))
			return false;
		if (tEmailDate == null) {
			if (other.tEmailDate != null)
				return false;
		} else if (!tEmailDate.equals(other.tEmailDate))
			return false;
		if (textIsHtml != other.textIsHtml)
			return false;
		if (toAddr == null) {
			if (other.toAddr != null)
				return false;
		} else if (!toAddr.equals(other.toAddr))
			return false;
		return true;
	}	
}
