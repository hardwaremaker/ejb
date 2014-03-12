/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.system.ejb;

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
@Table(name="LP_VERKEHRSWEG")
@Inheritance(strategy = InheritanceType.JOINED)
public class Verkehrsweg implements Serializable {
	private static final long serialVersionUID = 3270125347287691824L;

	@Id
	@Column(name="I_ID") 
	@TableGenerator(name="verkehrsweg_id", table="lp_primarykey", 
		pkColumnName = "c_name", pkColumnValue="verkehrsweg", valueColumnName="i_index", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="verkehrsweg_id")
	private Integer iId ;

	@Column(name="VERSANDWEG_I_ID")
	private Integer versandwegIId ;
	
	@Column(name="KUNDEN_I_ID")
	private Integer kundenIId ;

	public Integer getiId() {
		return iId;
	}

	public void setiId(Integer iId) {
		this.iId = iId;
	}

	public Integer getVersandwegIId() {
		return versandwegIId;
	}

	public void setVersandwegIId(Integer versandwegIId) {
		this.versandwegIId = versandwegIId;
	}

	public Integer getKundenIId() {
		return kundenIId;
	}

	public void setKundenIId(Integer kundenIId) {
		this.kundenIId = kundenIId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result
				+ ((kundenIId == null) ? 0 : kundenIId.hashCode());
		result = prime * result
				+ ((versandwegIId == null) ? 0 : versandwegIId.hashCode());
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
		Verkehrsweg other = (Verkehrsweg) obj;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (kundenIId == null) {
			if (other.kundenIId != null)
				return false;
		} else if (!kundenIId.equals(other.kundenIId))
			return false;
		if (versandwegIId == null) {
			if (other.versandwegIId != null)
				return false;
		} else if (!versandwegIId.equals(other.versandwegIId))
			return false;
		return true;
	}
	
}
