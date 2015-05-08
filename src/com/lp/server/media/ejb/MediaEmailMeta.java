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
package com.lp.server.media.ejb;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name=MediaEmailMetaQuery.ByUuid, 
			query = "SELECT OBJECT(C) FROM MediaEmailMeta c WHERE c.cUuid = :uuid AND c.bTyp = :btyp")
})

@Entity
@Table(name="MEDIA_EMAILMETA")
@PrimaryKeyJoinColumn(name="I_ID")
public class MediaEmailMeta extends MediaStore {
	private static final long serialVersionUID = 8866487913221139345L;
	
	@Column(name="C_FROM")
	private String cFrom ;

	@Column(name="C_TO")
	private String cTo ;

	@Column(name="C_CC")
	private String cCc ;

	@Column(name="C_BCC")
	private String cBcc ;

	@Column(name="C_SUBJECT")
	private String cSubject ;
	
	@Column(name = "T_EMAILDATE")
	private Timestamp tEmailDate ;
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen ;

	@Column(name = "B_PRIORITY")
	private Short bPriority ;
	
	@Column(name = "X_CONTENT")
	private String xContent ;

	@Column(name = "B_HTML")
	private Short bHtml ;
	
	@Column(name = "REPLY_MEDIA_I_ID")
	private Integer replyMediaIId ;

	
	public MediaEmailMeta() {
		setBTyp(Type.EMAIL);
		setBPriority(Priority.NORMAL);
	}
	
	public String getCFrom() {
		return cFrom;
	}

	public void setCFrom(String cFrom) {
		this.cFrom = getCutted(1024, cFrom) ;
	}

	public String getCTo() {
		return cTo;
	}

	public void setCTo(String cTo) {
		this.cTo = getCutted(1024, cTo);
	}

	public String getCCc() {
		return cCc;
	}

	public void setCCc(String cCc) {
		this.cCc = getCutted(1024, cCc);
	}

	public String getCBcc() {
		return cBcc;
	}

	public void setCBcc(String cBcc) {
		this.cBcc = getCutted(1024, cBcc);
	}

	public String getCSubject() {
		return cSubject;
	}

	public void setCSubject(String cSubject) {
		this.cSubject = getCutted(1024, cSubject);
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

	private String getCutted(int maxlength, String value) {
		if(value == null) return value ;
		if(value.length() <= maxlength) return value ;
		return value.substring(0, maxlength) ;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bHtml == null) ? 0 : bHtml.hashCode());
		result = prime * result
				+ ((bPriority == null) ? 0 : bPriority.hashCode());
		result = prime * result + ((cBcc == null) ? 0 : cBcc.hashCode());
		result = prime * result + ((cCc == null) ? 0 : cCc.hashCode());
		result = prime * result + ((cFrom == null) ? 0 : cFrom.hashCode());
		result = prime * result
				+ ((cSubject == null) ? 0 : cSubject.hashCode());
		result = prime * result + ((cTo == null) ? 0 : cTo.hashCode());
		result = prime * result
				+ ((replyMediaIId == null) ? 0 : replyMediaIId.hashCode());
		result = prime * result
				+ ((tAnlegen == null) ? 0 : tAnlegen.hashCode());
		result = prime * result
				+ ((tEmailDate == null) ? 0 : tEmailDate.hashCode());
		result = prime * result
				+ ((xContent == null) ? 0 : xContent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediaEmailMeta other = (MediaEmailMeta) obj;
		if (bHtml == null) {
			if (other.bHtml != null)
				return false;
		} else if (!bHtml.equals(other.bHtml))
			return false;
		if (bPriority == null) {
			if (other.bPriority != null)
				return false;
		} else if (!bPriority.equals(other.bPriority))
			return false;
		if (cBcc == null) {
			if (other.cBcc != null)
				return false;
		} else if (!cBcc.equals(other.cBcc))
			return false;
		if (cCc == null) {
			if (other.cCc != null)
				return false;
		} else if (!cCc.equals(other.cCc))
			return false;
		if (cFrom == null) {
			if (other.cFrom != null)
				return false;
		} else if (!cFrom.equals(other.cFrom))
			return false;
		if (cSubject == null) {
			if (other.cSubject != null)
				return false;
		} else if (!cSubject.equals(other.cSubject))
			return false;
		if (cTo == null) {
			if (other.cTo != null)
				return false;
		} else if (!cTo.equals(other.cTo))
			return false;
		if (replyMediaIId == null) {
			if (other.replyMediaIId != null)
				return false;
		} else if (!replyMediaIId.equals(other.replyMediaIId))
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
		if (xContent == null) {
			if (other.xContent != null)
				return false;
		} else if (!xContent.equals(other.xContent))
			return false;
		return true;
	}
}
