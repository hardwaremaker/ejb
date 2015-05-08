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
package com.lp.server.media.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


public class MediaEmailMetaDto implements Serializable {
	private static final long serialVersionUID = 8866487913221139345L;
	
	private String mandantCNr;
	private String cUuid ;
	private Integer iId ;
	private Short bTyp ;
	private Short bOnline;
	
	private String cFrom ;
	private String cTo ;
	private String cCc ;
	private String cBcc ;
	private String cSubject ;
	private Timestamp tEmailDate ;
	private Timestamp tAnlegen ;
	private Short bPriority ;
	private String xContent ;
	private Short bHtml ;
	private Integer replyMediaIId ;
	
	private List<MediaEmailAttachmentDto> attachments ;
	
	public String getMandantCNr() {
		return mandantCNr;
	}
	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}
	public String getUuid() {
		return cUuid;
	}
	public void setUuid(String cUuid) {
		this.cUuid = cUuid;
	}	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer mediaId) {
		this.iId = mediaId;
	}
	public Short getBTyp() {
		return bTyp;
	}
	public void setBTyp(Short bTyp) {
		this.bTyp = bTyp;
	}
	public Short getBOnline() {
		return bOnline;
	}
	public void setBOnline(Short bOnline) {
		this.bOnline = bOnline;
	}
	
	public String getCFrom() {
		return cFrom;
	}
	public void setCFrom(String cFrom) {
		this.cFrom = cFrom;
	}
	public String getCTo() {
		return cTo;
	}
	public void setCTo(String cTo) {
		this.cTo = cTo;
	}
	public String getCCc() {
		return cCc;
	}
	public void setCCc(String cCc) {
		this.cCc = cCc;
	}
	public String getCBcc() {
		return cBcc;
	}
	public void setCBcc(String cBcc) {
		this.cBcc = cBcc;
	}
	public String getCSubject() {
		return cSubject;
	}
	public void setCSubject(String cSubject) {
		this.cSubject = cSubject;
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
	public Short getBPriority() {
		return bPriority;
	}
	public void setBPriority(Short bPriority) {
		this.bPriority = bPriority;
	}
	public String getXContent() {
		return xContent;
	}
	public void setXContent(String xContent) {
		this.xContent = xContent;
	}
	public List<MediaEmailAttachmentDto> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<MediaEmailAttachmentDto> attachments) {
		this.attachments = attachments;
	}
	public Short getBHtml() {
		return bHtml;
	}
	public void setBHtml(Short bHtml) {
		this.bHtml = bHtml;
	}
	public Integer getReplyMediaIId() {
		return replyMediaIId;
	}
	public void setReplyMediaIId(Integer replyMediaIId) {
		this.replyMediaIId = replyMediaIId;
	}
}
