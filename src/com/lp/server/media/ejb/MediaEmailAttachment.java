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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@NamedQueries({
	@NamedQuery(name=MediaEmailAttachmentQuery.ByUuid, 
			query = "SELECT OBJECT(C) FROM MediaEmailAttachment c WHERE c.cUuid = :uuid"),
	@NamedQuery(name=MediaEmailAttachmentQuery.ByMediaId, 
			query = "SELECT OBJECT(C) FROM MediaEmailAttachment c WHERE c.mediaIId = :mediaid")
})

@Entity
@Table(name="MEDIA_EMAILATTACHMENT")
public class MediaEmailAttachment implements Serializable {
	private static final long serialVersionUID = -1160050848156773683L;

	@Id
	@Column(name="I_ID")
	@TableGenerator(name="mediaattachment_id", table="LP_PRIMARYKEY",
			pkColumnName = "C_NAME", pkColumnValue="media_attachment", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="mediaattachment_id")
	private Integer iId ;

	@Column(name="MEDIA_I_ID") 
	private Integer mediaIId ;
	
	@Column(name="C_UUID")
	private String cUuid ;
	
	@Column(name="C_NAME")
	private String cName ;
	
	@Column(name="C_DESCRIPTION")
	private String cDescription ;
	
	@Column(name="C_MIMETYPE")
	private String cMimeType ;
	
	@Column(name="I_SIZE") 
	private Integer iSize ;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getMediaIId() {
		return mediaIId;
	}

	public void setMediaIId(Integer mediaIId) {
		this.mediaIId = mediaIId;
	}

	public String getCUuid() {
		return cUuid;
	}

	public void setCUuid(String cUuid) {
		this.cUuid = cUuid;
	}

	public String getCName() {
		return cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public String getCDescription() {
		return cDescription;
	}

	public void setCDescription(String cDescription) {
		this.cDescription = cDescription;
	}

	public String getCMimeType() {
		return cMimeType;
	}

	public void setCMimeType(String cMimeType) {
		this.cMimeType = cMimeType;
	}

	public Integer getSize() {
		return iSize;
	}

	public void setSize(Integer iSize) {
		this.iSize = iSize;
	}	
}
