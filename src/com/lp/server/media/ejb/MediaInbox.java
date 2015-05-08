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
import java.sql.Timestamp;

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
	@NamedQuery(name=MediaInboxQuery.ByMediaIdPersonalId, 
			query = "SELECT OBJECT(C) FROM MediaInbox c WHERE c.mediaId = :mediaId AND c.personalId = :personalId")
})

@Entity
@Table(name="MEDIA_INBOX")
public class MediaInbox implements Serializable {
	private static final long serialVersionUID = 8840367107960015409L;

	@Id
	@Column(name="I_ID") 
	@TableGenerator(name="mediainbox_id", table="LP_PRIMARYKEY",
			pkColumnName = "C_NAME", pkColumnValue="media_inbox", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="mediainbox_id")
	private Integer iId ;

	@Column(name="MEDIA_I_ID")
	private Integer mediaId ;
	
	@Column(name="PERSONAL_I_ID")
	private Integer personalId ;
	
	@Column(name="STATUS_C_NR")
	private String statusCnr ;
	
	@Column(name="B_VERSTECKT")
	private Short bVersteckt ;
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "T_GELESEN")
	private Timestamp tGelesen ;
	
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Integer getPersonalIId() {
		return personalId;
	}

	public void setPersonalIId(Integer personalId) {
		this.personalId = personalId;
	}

	public String getStatusCnr() {
		return statusCnr;
	}

	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr;
	}

	public Short getBVersteckt() {
		return bVersteckt;
	}

	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTGelesen() {
		return tGelesen ;
	}

	public void setTGelesen(Timestamp tGelesen) {
		this.tGelesen = tGelesen ;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bVersteckt == null) ? 0 : bVersteckt.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((mediaId == null) ? 0 : mediaId.hashCode());
		result = prime
				* result
				+ ((personalIIdAendern == null) ? 0 : personalIIdAendern
						.hashCode());
		result = prime
				* result
				+ ((personalIIdAnlegen == null) ? 0 : personalIIdAnlegen
						.hashCode());
		result = prime * result
				+ ((personalId == null) ? 0 : personalId.hashCode());
		result = prime * result
				+ ((statusCnr == null) ? 0 : statusCnr.hashCode());
		result = prime * result
				+ ((tAendern == null) ? 0 : tAendern.hashCode());
		result = prime * result
				+ ((tAnlegen == null) ? 0 : tAnlegen.hashCode());
		result = prime * result
				+ ((tGelesen == null) ? 0 : tGelesen.hashCode());
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
		MediaInbox other = (MediaInbox) obj;
		if (bVersteckt == null) {
			if (other.bVersteckt != null)
				return false;
		} else if (!bVersteckt.equals(other.bVersteckt))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		if (personalIIdAendern == null) {
			if (other.personalIIdAendern != null)
				return false;
		} else if (!personalIIdAendern.equals(other.personalIIdAendern))
			return false;
		if (personalIIdAnlegen == null) {
			if (other.personalIIdAnlegen != null)
				return false;
		} else if (!personalIIdAnlegen.equals(other.personalIIdAnlegen))
			return false;
		if (personalId == null) {
			if (other.personalId != null)
				return false;
		} else if (!personalId.equals(other.personalId))
			return false;
		if (statusCnr == null) {
			if (other.statusCnr != null)
				return false;
		} else if (!statusCnr.equals(other.statusCnr))
			return false;
		if (tAendern == null) {
			if (other.tAendern != null)
				return false;
		} else if (!tAendern.equals(other.tAendern))
			return false;
		if (tAnlegen == null) {
			if (other.tAnlegen != null)
				return false;
		} else if (!tAnlegen.equals(other.tAnlegen))
			return false;
		if (tGelesen == null) {
			if (other.tGelesen != null)
				return false;
		} else if (!tGelesen.equals(other.tGelesen))
			return false;
		return true;
	}	
}
