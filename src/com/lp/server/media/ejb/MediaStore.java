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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@Entity
@Table(name="MEDIA_STORE")
@Inheritance(strategy = InheritanceType.JOINED)
public class MediaStore implements Serializable {
	private static final long serialVersionUID = 8148263364068713429L;

	public static class Type {
		public final static Short EMAIL = Short.valueOf((short)1) ;
	}
	
	public static class Priority {
		public final static Short LOW = Short.valueOf((short)-1) ;
	
		public final static Short NORMAL = Short.valueOf((short)0) ;
		public final static Short HIGH = Short.valueOf((short)1) ;
	}
	
	@Id
	@Column(name="I_ID") 
	@TableGenerator(name="mediastore_id", table="LP_PRIMARYKEY",
			pkColumnName = "C_NAME", pkColumnValue="media_store", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="mediastore_id")
	private Integer iId ;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	
	@Column(name = "C_UUID")
	private String cUuid ;
	
	@Column(name = "B_TYP")
	private Short bTyp ;

	@Column(name = "B_ONLINE")
	private Short bOnline;

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bOnline == null) ? 0 : bOnline.hashCode());
		result = prime * result + ((bTyp == null) ? 0 : bTyp.hashCode());
		result = prime * result + ((cUuid == null) ? 0 : cUuid.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result
				+ ((mandantCNr == null) ? 0 : mandantCNr.hashCode());
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
		MediaStore other = (MediaStore) obj;
		if (bOnline == null) {
			if (other.bOnline != null)
				return false;
		} else if (!bOnline.equals(other.bOnline))
			return false;
		if (bTyp == null) {
			if (other.bTyp != null)
				return false;
		} else if (!bTyp.equals(other.bTyp))
			return false;
		if (cUuid == null) {
			if (other.cUuid != null)
				return false;
		} else if (!cUuid.equals(other.cUuid))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (mandantCNr == null) {
			if (other.mandantCNr != null)
				return false;
		} else if (!mandantCNr.equals(other.mandantCNr))
			return false;
		return true;
	}
	
}
